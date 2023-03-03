package org.goafabric.personservice.persistence.audit;

import lombok.NonNull;
import lombok.SneakyThrows;

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

    @SneakyThrows
    public void insertAudit(AuditBean.AuditEvent auditEvent, Object object) { //we cannot use jpa because of the dynamic table name
        final String sql = "INSERT INTO " + getTableName(object) + "_audit"
                +  " (id, tenant_id, reference_id, operation, created_by, created_at, modified_by, modified_at, oldvalue, newvalue)"
                +  " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, auditEvent.getId());
            ps.setString(2, auditEvent.getTenantId());
            ps.setString(3, auditEvent.getReferenceId());
            ps.setString(4, String.valueOf(auditEvent.getOperation()));
            ps.setString(5, auditEvent.getCreatedBy());
            ps.setDate(6,
                    auditEvent.getCreatedAt() != null ? new Date(auditEvent.getCreatedAt().getTime()) : null);
            ps.setString(7, auditEvent.getModifiedBy());
            ps.setDate(8,
                    auditEvent.getModifiedAt() != null ? new Date(auditEvent.getModifiedAt().getTime()) : null);
            ps.setString(9, auditEvent.getOldValue());
            ps.setString(10, auditEvent.getNewValue());
            ps.executeUpdate();
        }
    }

    private String getTableName(@NonNull Object object) {
        return object.getClass().getSimpleName().replaceAll("Bo", "").toLowerCase();
    }
}