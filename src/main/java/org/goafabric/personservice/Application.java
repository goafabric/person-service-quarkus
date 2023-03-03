package org.goafabric.personservice;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.security.spi.runtime.AuthorizationController;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.goafabric.personservice.persistence.DatabaseProvisioning;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

@QuarkusMain
public class Application {

    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {
        private final DatabaseProvisioning databaseProvisioning;

        public MyApp(DatabaseProvisioning databaseProvisioning) {
            this.databaseProvisioning = databaseProvisioning;
        }

        @Override
        public int run(String... args) throws Exception {
            databaseProvisioning.run();
            Quarkus.waitForExit();
            return 0;
        }
    }

    @Getter
    @Alternative
    @ApplicationScoped
    @Priority(Interceptor.Priority.LIBRARY_AFTER)
    static class SecurityConfiguration extends AuthorizationController {
        @ConfigProperty(name = "security.authentication.enabled", defaultValue = "true")
        boolean isAuthorizationEnabled;
    }

}
