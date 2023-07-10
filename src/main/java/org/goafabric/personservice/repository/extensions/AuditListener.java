package org.goafabric.personservice.repository.extensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.ConfigProvider;
import org.goafabric.personservice.extensions.HttpInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.UUID;

/**
 * Specific Listener for JPA for Auditing
 *
 */

@RegisterForReflection
public class AuditListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private enum DbOperation { CREATE, READ, UPDATE, DELETE }

    record AuditEvent (
            String id,
            String referenceId,
            String type,
            DbOperation operation,
            String createdBy,
            Date createdAt,
            String modifiedBy,
            Date   modifiedAt,
            String oldValue,
            String newValue
    ) {}


    @PostLoad
    public void afterRead(Object object) {
        insertAudit(DbOperation.READ, getId(object), object, object);
    }

    @PostPersist
    public void afterCreate(Object object)  {
        insertAudit(DbOperation.CREATE, getId(object), null, object);
    }

    @PostUpdate
    public void afterUpdate(Object object) {
        final String id = getId(object);
        insertAudit(DbOperation.UPDATE, id,
                CDI.current().select(AuditJpaUpdater.class).get().findOldObject(object.getClass(), id), object);
    }

    @PostRemove
    public void afterDelete(Object object) {
        insertAudit(DbOperation.DELETE, getId(object), object, null);
    }

    private void insertAudit(final DbOperation operation, String referenceId, final Object oldObject, final Object newObject) {
        try {
            var auditEvent = createAuditEvent(operation, referenceId, oldObject, newObject);
            CDI.current().select(AuditJpaInserter.class).get().insertAudit(auditEvent, oldObject != null ? oldObject : newObject);
            log.debug("New audit event :\n{}", auditEvent);
        } catch (Exception e) {
            log.error("Error during audit:\n{}", e.getMessage(), e);
        }
    }

    private AuditEvent createAuditEvent(
            DbOperation dbOperation, String referenceId, final Object oldObject, final Object newObject) throws JsonProcessingException {
        final Date date = new Date(System.currentTimeMillis());
        return new AuditEvent(
                UUID.randomUUID().toString(),
                referenceId,
                newObject != null ? newObject.getClass().getSimpleName() : oldObject.getClass().getSimpleName(),
                dbOperation,
                (dbOperation == DbOperation.CREATE ? HttpInterceptor.getUserName() : null),
                (dbOperation == DbOperation.CREATE ? date : null),
                ((dbOperation == DbOperation.UPDATE || dbOperation == DbOperation.DELETE) ? HttpInterceptor.getUserName() : null),
                ((dbOperation == DbOperation.UPDATE || dbOperation == DbOperation.DELETE) ? date : null),
                (oldObject == null ? null : getJsonValue(oldObject)),
                (newObject == null ? null : getJsonValue(newObject))
        );
    }

    private String getJsonValue(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
    
    @ApplicationScoped @Unremovable
    static class AuditJpaUpdater {
        private final EntityManager entityManager;

        public AuditJpaUpdater(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        @Transactional(value = Transactional.TxType.REQUIRES_NEW) //new transaction helps us to retrieve the old value still inside the db
        public <T> T findOldObject(Class<T> clazz, String id) {
            return entityManager.find(clazz, id);
        }
    }

    @ApplicationScoped @Unremovable
    static class AuditJpaInserter  {
        private final DataSource dataSource;

        public AuditJpaInserter(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public void insertAudit(AuditEvent auditEvent, Object object) { //we cannot use jpa because of the dynamic table name
            try {
                final String sql = "INSERT INTO " + getTableName(object) + "_audit"
                        + " (id, reference_id, operation, created_by, created_at, modified_by, modified_at, oldvalue, newvalue)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, auditEvent.id());
                    ps.setString(2, auditEvent.referenceId());
                    ps.setString(3, String.valueOf(auditEvent.operation()));
                    ps.setString(4, auditEvent.createdBy());
                    ps.setDate(5, auditEvent.createdAt() != null ? new java.sql.Date(auditEvent.createdAt().getTime()) : null);
                    ps.setString(6, auditEvent.modifiedBy());
                    ps.setDate(7, auditEvent.modifiedAt() != null ? new java.sql.Date(auditEvent.modifiedAt().getTime()) : null);
                    ps.setString(8, auditEvent.oldValue());
                    ps.setString(9, auditEvent.newValue());
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private String getTableName(Object object) {
            final String schema = ConfigProvider.getConfig().getValue("multi-tenancy.schema-prefix", String.class) + HttpInterceptor.getTenantId() + ".";
            return object.getClass().getSimpleName().replaceAll("Eo", "").toLowerCase();
        }
    }

    private static String getId(Object object) {
        return String.valueOf(CDI.current().select(EntityManagerFactory.class).get().getPersistenceUnitUtil().getIdentifier(object));
    }
}
