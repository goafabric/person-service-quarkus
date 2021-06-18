package org.goafabric.personservice.crossfunctional;

import io.quarkus.security.spi.runtime.AuthorizationController;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

@Alternative
@Priority(Interceptor.Priority.LIBRARY_AFTER)
@ApplicationScoped
@Getter
public class SecurityConfiguration extends AuthorizationController {
    @ConfigProperty(name = "security.authentication.enabled", defaultValue = "true")
    boolean isAuthorizationEnabled;
}