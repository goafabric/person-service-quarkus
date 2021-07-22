package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.goafabric.personservice.crossfunctional.TenantIdInterceptor;

import java.util.Map;

public abstract class MultiTenantRepository <Entity extends TenantAware, Id> implements PanacheRepositoryBase<Entity, Id>{


    public PanacheQuery<Entity> findAllx() {
        return find("tenantId", TenantIdInterceptor.getTenantId());
    }

    public PanacheQuery<Entity> findx(String query, Object... params) {
        return find(query, params);
    }

    public PanacheQuery<Entity> findx(String query, Map<String, Object> params) {
        return find(query, params);
    }

    public Entity savex(Entity entity) {
        persist(entity);
        return entity;
    }
}
