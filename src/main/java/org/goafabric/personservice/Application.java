package org.goafabric.personservice;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.security.spi.runtime.AuthorizationController;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;

@QuarkusMain
public class Application {

    public static void main(String... args) {
        Quarkus.run(args);
    }

    @Alternative
    @ApplicationScoped @Priority(Interceptor.Priority.LIBRARY_AFTER)
    static class SecurityConfiguration extends AuthorizationController {
        @ConfigProperty(name = "security.authentication.enabled", defaultValue = "true")
        boolean isAuthorizationEnabled;
        public boolean isAuthorizationEnabled() { return isAuthorizationEnabled; }
    }

}
