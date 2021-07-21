package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.inject.Inject;

public abstract class MultiTenantRepository <Entity extends TenantAware, Id> {

    private class RepositoryDelegate implements PanacheRepositoryBase<Entity, Id>{}

    @Inject
    RepositoryDelegate repository;

    public PanacheQuery<Entity> findAll() {
        return find("tenantId", "0");
    }

    public PanacheQuery<Entity> find(String query, Object... params) {
        return repository.find(query, params);
    }

    public Entity save(Entity entity) {
        repository.persist(entity);
        return entity;
    }
}
