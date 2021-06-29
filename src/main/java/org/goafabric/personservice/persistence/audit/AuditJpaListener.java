package org.goafabric.personservice.persistence.audit;

import lombok.NonNull;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.*;
import javax.sql.DataSource;
import javax.transaction.Transactional;

/**
 * Specific Listener for JPA for Auditing
 *
 */

public class AuditJpaListener {

    @PostLoad
    public void afterRead(Object object) {
        CDI.current().select(AuditBean.class).get().afterRead(object, getId(object));
    }

    @PostPersist
    public void afterCreate(Object object)  {
        CDI.current().select(AuditBean.class).get().afterCreate(object, getId(object));
    }

    @PostUpdate
    public void afterUpdate(Object object) {
        CDI.current().select(AuditBean.class).get().afterUpdate(object, getId(object),
                CDI.current().select(AuditJpaUpdater.class).get().findOldObject(object.getClass(), getId(object)));
    }

    @PostRemove
    public void afterDelete(Object object) {
        CDI.current().select(AuditBean.class).get().afterDelete(object, getId(object));
    }

    private String getId(@NonNull Object object) {
        final TenantAware tenantAware = (TenantAware) object;
        return tenantAware.getId();
    }

    @ApplicationScoped
    static class AuditJpaUpdater {
        @Inject
        private EntityManager entityManager;

        @Transactional(value = Transactional.TxType.REQUIRES_NEW) //new transaction helps us to retrieve the old value still inside the db
        public <T> T findOldObject(Class<T> clazz, String id) {
            return entityManager.find(clazz, id);
        }
    }

    @ApplicationScoped
    static class AuditJpaInserter implements AuditBean.AuditInserter {
        @Inject DataSource dataSource;

        public void insertAudit(AuditBean.AuditEvent auditEvent, Object object) { //we cannot use jpa because of the dynamic table name
            System.out.println(auditEvent);
            //final SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource).withTableName(getTableName(object) + "_audit");
            //insert.execute(new BeanPropertySqlParameterSource(auditEvent));
        }

        private String getTableName(@NonNull Object object) {
            return object.getClass().getSimpleName().replaceAll("Bo", "").toLowerCase();
            //return object.getClass().getAnnotation(javax.persistence.Table.class).name();
        }
    }
}
