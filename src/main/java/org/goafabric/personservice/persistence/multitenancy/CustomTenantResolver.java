package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.goafabric.personservice.crossfunctional.HttpInterceptor;

@PersistenceUnitExtension
@RequestScoped
public class CustomTenantResolver implements TenantResolver {
    @Inject FlywayConfig flywayConfig;

    @Override
    public String getDefaultTenantId() {
        return "PUBLIC";
    }

    @Override
    public String resolveTenantId() {
        return "tenant_" + HttpInterceptor.getTenantId();
    }

}