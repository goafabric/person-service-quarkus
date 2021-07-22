package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.goafabric.personservice.crossfunctional.TenantIdInterceptor;

import java.util.HashMap;
import java.util.Map;

public abstract class MultiTenantRepository <Entity extends TenantAware, Id> implements PanacheRepositoryBase<Entity, Id>{

    public PanacheQuery<Entity> findAllx() {
        return findx("", new HashMap<>());
    }

    public PanacheQuery<Entity> findx(String query, Map<String, Object> params) {
        final Map<String, Object> map = new HashMap<>(params);
        map.put("tenantId", TenantIdInterceptor.getTenantId());
        if (params.size() > 0) {
            query += " and ";
        }
        return find(query + "tenantId = :tenantId", map);
    }

    public Entity savex(Entity entity) {
        persist(entity);
        return entity;
    }
}
