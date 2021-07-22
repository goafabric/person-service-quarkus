package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public abstract class MultiTenantRepository <Entity extends TenantAware, Id> implements PanacheRepositoryBase<Entity, Id>{


    public PanacheQuery<Entity> findAllx() {
        return find("tenantId", "0");
    }

    public PanacheQuery<Entity> findx(String query, Object... params) {
        return find(query, params);
    }

    public Entity savex(Entity entity) {
        persist(entity);
        return entity;
    }
}
