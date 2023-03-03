package org.goafabric.personservice.persistence.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.Unremovable;
import io.quarkus.security.identity.SecurityIdentity;
import lombok.Builder;
import lombok.Data;
import org.goafabric.personservice.crossfunctional.HttpInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.UUID;

@Unremovable
@ApplicationScoped
/** A class that audits all registered entities with @EntityListeners and writes the Audit Entries to the database **/
public class AuditBean {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private enum DbOperation {
        CREATE, READ, UPDATE, DELETE
    }

    @Data
    @Builder
    static class AuditEvent {
        private String id;
        private String tenantId;
        private String referenceId;
        private String type;
        private DbOperation operation;
        private String createdBy;
        private Date createdAt;
        private String modifiedBy;
        private Date   modifiedAt;
        private String oldValue;
        private String newValue;
    }

    interface AuditInserter {
        void insertAudit(AuditEvent auditEvent, Object object);
    }

    private final AuditInserter auditInserter;

    private final SecurityIdentity securityIdentity;

    public AuditBean(AuditInserter auditInserter, SecurityIdentity securityIdentity) {
        this.auditInserter = auditInserter;
        this.securityIdentity = securityIdentity;
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
            final AuditEvent auditEvent =
                createAuditEvent(operation, referenceId, oldObject, newObject);
            auditInserter.insertAudit(auditEvent, oldObject != null ? oldObject : newObject);
            log.debug("New audit event :\n{}", auditEvent);
        } catch (Exception e) {
            log.error("Error during audit:\n{}", e.getMessage(), e);
        }
    }
    
    private AuditEvent createAuditEvent(
            DbOperation dbOperation, String referenceId, final Object oldObject, final Object newObject) throws JsonProcessingException {
        final Date date = new Date(System.currentTimeMillis());
        return AuditEvent.builder()
                .id(UUID.randomUUID().toString())
                .referenceId(referenceId)
                .tenantId(HttpInterceptor.getTenantId())
                .operation(dbOperation)
                .type(newObject.getClass().getSimpleName())
                .createdBy(dbOperation == DbOperation.CREATE ? HttpInterceptor.getUserName() : null)
                .createdAt(dbOperation == DbOperation.CREATE ? date : null)
                .modifiedBy((dbOperation == DbOperation.UPDATE || dbOperation == DbOperation.DELETE) ? HttpInterceptor.getUserName() : null)
                .modifiedAt((dbOperation == DbOperation.UPDATE || dbOperation == DbOperation.DELETE) ? date : null)
                .oldValue(oldObject == null ? null : getJsonValue(oldObject))
                .newValue(newObject == null ? null : getJsonValue(newObject))
                .build();
    }

    private String getJsonValue(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
