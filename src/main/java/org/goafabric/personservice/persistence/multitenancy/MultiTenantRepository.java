package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public abstract class MultiTenantRepository <Entity extends TenantAware, Id> implements PanacheRepositoryBase<Entity, Id>{


    public PanacheQuery<Entity> findAll() {
        return find("tenantId", "0");
    }

    public PanacheQuery<Entity> find(String query, Object... params) {
        return find(query, params);
    }

    public Entity save(Entity entity) {
        persist(entity);
        return entity;
    }
}
