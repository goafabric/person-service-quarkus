package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RepositoryDelegate<T extends TenantAware, I> implements PanacheRepositoryBase<T, I> {
}
