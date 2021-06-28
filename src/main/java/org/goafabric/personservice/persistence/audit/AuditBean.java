package org.goafabric.personservice.persistence.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.security.identity.SecurityIdentity;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.goafabric.personservice.crossfunctional.TenantFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

@Slf4j
@ApplicationScoped
//also see https://www.baeldung.com/database-auditing-jpa
/**
 * A class that audits all registered entities with @EntityListeners
 * And writes the Audit Entries to the database
 */
public class AuditBean {
    private enum DbOperation {
        CREATE,
        READ,
        UPDATE,
        DELETE
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

    @Inject
    private AuditInserter auditInserter;

    @Inject
    SecurityIdentity securityIdentity;


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
            logAudit(auditEvent);
        } catch (Exception e) {
            log.error("Error during audit:\n{}", e.getMessage(), e);
        }
    }

    private void logAudit(AuditEvent auditEvent) {
        if (log.isDebugEnabled()) {
            auditEvent.setOldValue(auditEvent.getOldValue());
            auditEvent.setNewValue(auditEvent.getNewValue());
            log.debug("New audit event :\n{}", auditEvent.toString());
        }
    }

    private AuditEvent createAuditEvent(
            DbOperation dbOperation, String referenceId, final Object oldObject, final Object newObject) throws JsonProcessingException {
        final Date date = new Date(System.currentTimeMillis());
        return AuditEvent.builder()
                .id(UUID.randomUUID().toString())
                .referenceId(referenceId)
                .tenantId(TenantFilter.getTenantId())
                .operation(dbOperation)
                .createdBy(dbOperation == DbOperation.CREATE ? getUserName() : null)
                .createdAt(dbOperation == DbOperation.CREATE ? date : null)
                .modifiedBy(dbOperation == DbOperation.CREATE ? null : getUserName())
                .modifiedAt(dbOperation == DbOperation.CREATE ? null : date)
                .oldValue(oldObject == null ? null : getJsonValue(oldObject))
                .newValue(newObject == null ? null : getJsonValue(newObject))
                .build();
    }

    private String getUserName() {
        final Principal authentication = securityIdentity.getPrincipal();
        return (authentication == null) ? "" : authentication.getName();
    }

    private String getJsonValue(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
