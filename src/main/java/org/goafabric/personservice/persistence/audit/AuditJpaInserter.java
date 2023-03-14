package org.goafabric.personservice.persistence.audit;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

@ApplicationScoped
public class AuditJpaInserter implements AuditBean.AuditInserter {
    private final DataSource dataSource;

    public AuditJpaInserter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertAudit(AuditBean.AuditEvent auditEvent, Object object) { //we cannot use jpa because of the dynamic table name
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
                        auditEvent.createdAt() != null ? new Date(auditEvent.createdAt().getTime()) : null);
                ps.setString(7, auditEvent.modifiedBy());
                ps.setDate(8,
                        auditEvent.modifiedAt() != null ? new Date(auditEvent.modifiedAt().getTime()) : null);
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