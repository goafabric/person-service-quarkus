package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import org.goafabric.personservice.crossfunctional.TenantIdInterceptor;

import java.util.HashMap;
import java.util.Map;

public abstract class MultiTenantRepository <Entity extends TenantAware, Id> implements PanacheRepositoryBase<Entity, Id>{

    public PanacheQuery<Entity> findAllx() {
        return findx("", new HashMap<>());
    }

    public PanacheQuery<Entity> findAllx(Sort sort) {
        return findx("", sort, new HashMap<>());
    }

    public PanacheQuery<Entity> findByIdx(Object id) {
        return findx("id", id);
    }

    public PanacheQuery<Entity> findx(String field, Object param) {
        return findx(field + " = :" + field, null, Parameters.with(field, param).map());
    }

    public PanacheQuery<Entity> findx(String field, Sort sort, Object param) {
        return findx(field + " = :" + field, sort, Parameters.with(field, param).map());
    }
    
    public PanacheQuery<Entity> findx(String query, Map<String, Object> params) {
        return findx(query, null, params);
    }

    public PanacheQuery<Entity> findx(String query, Sort sort, Map<String, Object> params) {
        return find(getTenantQuery(query, params), sort, getTenantParams(params));
    }

    public long countx(String query, Map<String, Object> params) {
        return count(getTenantQuery(query, params), getTenantParams(params));
    }

    public long deletex(String query, Map<String, Object> params) {
        return delete(getTenantQuery(query, params), getTenantParams(params));
    }

    public Entity savex(Entity entity) {
        persist(entity);
        return entity;
    }

    private Map<String, Object> getTenantParams(Map<String, Object> params) {
        final Map<String, Object> map = new HashMap<>(params);
        map.put("tenantId", TenantIdInterceptor.getTenantId());
        return map;
    }

    private String getTenantQuery(String query, Map<String, Object> params) {
        return (params.size() > 0)  ? query + " and tenantId = :tenantId"
                                    : "tenantId = :tenantId";
    }

}
