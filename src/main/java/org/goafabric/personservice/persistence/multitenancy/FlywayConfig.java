package org.goafabric.personservice.persistence.multitenancy;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import org.eclipse.microprofile.config.ConfigProvider;
import org.flywaydb.core.Flyway;

import java.util.Arrays;
import java.util.Map;

@ApplicationScoped
public class FlywayConfig {
    public FlywayConfig() {
        configFly();
    }
    private void configFly() {
        final Flyway flyway = CDI.current().select(Flyway.class).get();
        final String schemas = ConfigProvider.getConfig().getValue("quarkus.flyway.schemas", String.class);
        //String schemas = "tenant_0,tenant_5a2f";
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
