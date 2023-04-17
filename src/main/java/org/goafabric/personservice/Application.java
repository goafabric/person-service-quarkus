package org.goafabric.personservice;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.security.spi.runtime.AuthorizationController;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@QuarkusMain
public class Application {

    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            //doFlyway();
            Quarkus.waitForExit();
            return 0;
        }

        /*
        public static void doFlyway() {
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

         */
    }

    @Alternative
    @ApplicationScoped @Priority(Interceptor.Priority.LIBRARY_AFTER)
    static class SecurityConfiguration extends AuthorizationController {
        @ConfigProperty(name = "security.authentication.enabled", defaultValue = "true")
        boolean isAuthorizationEnabled;
        public boolean isAuthorizationEnabled() { return isAuthorizationEnabled; }
    }
    

}
