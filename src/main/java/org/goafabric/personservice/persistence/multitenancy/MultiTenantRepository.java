package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import org.goafabric.personservice.crossfunctional.TenantIdInterceptor;

import java.util.HashMap;
import java.util.Map;

public abstract class MultiTenantRepository <Entity extends TenantAware> {

    PanacheRepositoryBase repository;

    public <T extends PanacheRepositoryBase> MultiTenantRepository(PanacheRepositoryBase repositoryDelegate) {
        this.repository = repositoryDelegate;
    }

    public MultiTenantRepository() {
    }

    public PanacheQuery<Entity> findAll() {
        return find("", new HashMap<>());
    }

    public PanacheQuery<Entity> findAll(Sort sort) {
        return find("", sort, new HashMap<>());
    }

    public PanacheQuery<Entity> findById(Object id) {
        return find("id", id);
    }

    public PanacheQuery<Entity> find(String field, Object param) {
        return find(field + " = :" + field, null, Parameters.with(field, param).map());
    }

    public PanacheQuery<Entity> find(String field, Sort sort, Object param) {
        return find(field + " = :" + field, sort, Parameters.with(field, param).map());
    }
    
    public PanacheQuery<Entity> find(String query, Map<String, Object> params) {
        return find(query, null, params);
    }

    public PanacheQuery<Entity> find(String query, Sort sort, Map<String, Object> params) {
        return repository.find(getTenantQuery(query, params), sort, getTenantParams(params));
    }

    public long count(String query, Map<String, Object> params) {
        return repository.count(getTenantQuery(query, params), getTenantParams(params));
    }

    public long delete(String query, Map<String, Object> params) {
        return repository.delete(getTenantQuery(query, params), getTenantParams(params));
    }

    public Entity save(Entity entity) {
        repository.persist(entity);
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
