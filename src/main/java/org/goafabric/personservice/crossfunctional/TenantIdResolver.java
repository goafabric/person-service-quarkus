package org.goafabric.personservice.crossfunctional;

import io.quarkus.oidc.OidcTenantConfig;
import io.quarkus.oidc.TenantConfigResolver;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Slf4j
public class TenantIdResolver implements TenantConfigResolver {

    @Override
    public Uni<OidcTenantConfig> resolve(RoutingContext routingContext, TenantConfigResolver.TenantConfigRequestContext requestContext) {
        final String tenantId = routingContext.request().getHeader("X-TenantId");
        return Uni.createFrom().item(createOidcConfig(tenantId == null ? "0" : tenantId));
    }

    private OidcTenantConfig createOidcConfig(String tenantId) {
        final OidcTenantConfig tenantConfig = new OidcTenantConfig();
        final Config config = ConfigProvider.getConfig();

        tenantConfig.setTenantId(tenantId);
        tenantConfig.setApplicationType(
                OidcTenantConfig.ApplicationType.valueOf(config.getValue("quarkus.oidc.application-type", String.class).toUpperCase()));
        tenantConfig.setRoles(getRolesConfig(config));
        tenantConfig.setClientId(config.getValue("quarkus.oidc.client-id", String.class));
        tenantConfig.setAuthServerUrl(config.getValue("quarkus.oidc.auth-server-url", String.class) + tenantId);
        return tenantConfig;
    }

    private OidcTenantConfig.Roles getRolesConfig(Config config) {
        final OidcTenantConfig.Roles roles = new OidcTenantConfig.Roles();
        roles.setSource(config.getValue("quarkus.oidc.roles.source", OidcTenantConfig.Roles.Source.class));
        return roles;
    }
}
