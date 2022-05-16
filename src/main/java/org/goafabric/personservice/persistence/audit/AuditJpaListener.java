package org.goafabric.personservice.persistence.audit;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;

/**
 * Specific Listener for JPA for Auditing
 *
 */

@RegisterForReflection
public class AuditJpaListener {

    @PostLoad
    public void afterRead(Object object) {
        CDI.current().select(AuditBean.class).get().afterRead(object, ((AuditAware) object).getId());
    }

    @PostPersist
    public void afterCreate(Object object)  {
        CDI.current().select(AuditBean.class).get().afterCreate(object, ((AuditAware) object).getId());
    }

    @PostUpdate
    public void afterUpdate(Object object) {
        CDI.current().select(AuditBean.class).get().afterUpdate(object, ((AuditAware) object).getId(),
                CDI.current().select(AuditJpaUpdater.class).get().findOldObject(object.getClass(), ((AuditAware) object).getId()));
    }

    @PostRemove
    public void afterDelete(Object object) {
        CDI.current().select(AuditBean.class).get().afterDelete(object, ((AuditAware) object).getId());
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

}
