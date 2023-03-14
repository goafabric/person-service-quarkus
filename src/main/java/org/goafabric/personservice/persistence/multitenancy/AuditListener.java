package org.goafabric.personservice.persistence.multitenancy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.goafabric.personservice.crossfunctional.HttpInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.*;
import javax.sql.DataSource;
import javax.transaction.Transactional;
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
            String tenantId,
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
        afterRead(object, ((TenantAware) object).getId());
    }

    @PostPersist
    public void afterCreate(Object object)  {
        afterCreate(object, ((TenantAware) object).getId());
    }

    @PostUpdate
    public void afterUpdate(Object object) {
        afterUpdate(object, ((TenantAware) object).getId(),
                CDI.current().select(AuditJpaUpdater.class).get().findOldObject(object.getClass(), ((TenantAware) object).getId()));
    }

    @PostRemove
    public void afterDelete(Object object) {
        afterDelete(object, ((TenantAware) object).getId());
    }

    public void afterRead(Object object, String id) {
        insertAudit(DbOperation.READ, id, object, object);
    }

    public void afterCreate(Object object, String id) {
        insertAudit(DbOperation.CREATE, id, null, object);
    }

    public void afterUpdate(Object object, String id, Object oldObject) {
        insertAudit(DbOperation.UPDATE, id, oldObject, object);
    }

    public void afterDelete(Object object, String id) {
        insertAudit(DbOperation.DELETE, id, object, null);
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
                HttpInterceptor.getTenantId(),
                referenceId,
                newObject.getClass().getSimpleName(),
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
    
    @ApplicationScoped
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

    @ApplicationScoped
    @Unremovable
    static class AuditJpaInserter  {
        private final DataSource dataSource;

        public AuditJpaInserter(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public void insertAudit(AuditListener.AuditEvent auditEvent, Object object) { //we cannot use jpa because of the dynamic table name
            try {
                final String sql = "INSERT INTO " + getTableName(object) + "_audit"
                        + " (id, tenant_id, reference_id, operation, created_by, created_at, modified_by, modified_at, oldvalue, newvalue)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, auditEvent.id());
                    ps.setString(2, auditEvent.tenantId());
                    ps.setString(3, auditEvent.referenceId());
                    ps.setString(4, String.valueOf(auditEvent.operation()));
                    ps.setString(5, auditEvent.createdBy());
                    ps.setDate(6,
                            auditEvent.createdAt() != null ? new java.sql.Date(auditEvent.createdAt().getTime()) : null);
                    ps.setString(7, auditEvent.modifiedBy());
                    ps.setDate(8,
                            auditEvent.modifiedAt() != null ? new java.sql.Date(auditEvent.modifiedAt().getTime()) : null);
                    ps.setString(9, auditEvent.oldValue());
                    ps.setString(10, auditEvent.newValue());
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private String getTableName(Object object) {
            return object.getClass().getSimpleName().replaceAll("Bo", "").toLowerCase();
        }
    }

}
