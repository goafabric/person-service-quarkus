package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.ConfigProvider;
import org.flywaydb.core.Flyway;
import org.goafabric.personservice.crossfunctional.HttpInterceptor;

import java.util.Arrays;
import java.util.Map;

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

    @ApplicationScoped
    static class FlywayConfig {
        public FlywayConfig() {
            final Flyway flyway = CDI.current().select(Flyway.class).get();
            final String schemas = ConfigProvider.getConfig().getValue("quarkus.flyway.schemas", String.class);
            String schema_prefix = "";
            Arrays.asList(schemas.split(",")).forEach(schema -> {
                        Flyway.configure()
                                .configuration(flyway.getConfiguration())
                                .schemas(schema_prefix + schema)
                                .defaultSchema(schema_prefix + schema)
                                .placeholders(Map.of("tenantId", schema))
                                .load()
                                .migrate();
                    }
            );
        }
    }


}