package org.goafabric.personservice.adapter;

import io.quarkus.oidc.token.propagation.AccessTokenRequestFilter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.goafabric.personservice.crossfunctional.TenantIdInterceptor;

import javax.ws.rs.client.ClientRequestContext;
import java.io.IOException;

public class ConfigurableAccessTokenRequestFilter extends AccessTokenRequestFilter {
    @ConfigProperty(name = "security.authentication.enabled")
    boolean isAuthorizationEnabled;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if (isAuthorizationEnabled) { super.filter(requestContext); }
        requestContext.getStringHeaders().add("X-TenantId", TenantIdInterceptor.getTenantId());
    }
}