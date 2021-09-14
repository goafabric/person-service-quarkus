package org.goafabric.personservice.crossfunctional;

import io.quarkus.oidc.OidcTenantConfig;
import io.quarkus.oidc.TenantConfigResolver;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Slf4j
public class TenantIdResolver implements TenantConfigResolver {

    @Override
    public Uni<OidcTenantConfig> resolve(RoutingContext routingContext, TenantConfigResolver.TenantConfigRequestContext requestContext) {
        String tenantId = routingContext.request().getHeader("X-TenantId");
        log.info("#Tenantresolver got: " + tenantId);

        if (tenantId == null) {
            tenantId = "0";
        }

        final OidcTenantConfig config = new OidcTenantConfig();
        //config.setApplicationType(ConfigProvider.getConfig().getValue("quarkus.oidc.application-type", OidcTenantConfig.ApplicationType.class));
        config.setApplicationType(OidcTenantConfig.ApplicationType.HYBRID);

        //config.setRoles(OidcTenantConfig.Roles.Source.accesstoken);
        config.setClientId(ConfigProvider.getConfig().getValue("quarkus.oidc.client-id", String.class));

        config.setTenantId("tenant-" + tenantId);
        config.setAuthServerUrl(
                ConfigProvider.getConfig().getValue("quarkus.oidc.auth-server-url", String.class) + "/" + config.getTenantId().get());

        return Uni.createFrom().item(config);
    }
}
