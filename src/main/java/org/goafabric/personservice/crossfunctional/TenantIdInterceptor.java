package org.goafabric.personservice.crossfunctional;

import io.quarkus.oidc.OidcTenantConfig;
import io.quarkus.oidc.TenantConfigResolver;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Slf4j
@Provider
@ApplicationScoped
public class TenantIdInterceptor implements ContainerRequestFilter, ContainerResponseFilter, TenantConfigResolver {
    private static final ThreadLocal<String> tenantIdThreadLocal = new ThreadLocal<>();
    public static final String TENANT_ID = "X-TenantId";

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        setTenantId(request.getHeaderString(TENANT_ID));
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        tenantIdThreadLocal.remove();
    }

    @Override
    public Uni<OidcTenantConfig> resolve(RoutingContext routingContext, TenantConfigResolver.TenantConfigRequestContext requestContext) {
        return createOidcConfig(getTenantId(routingContext.request().getHeader(TENANT_ID)));
    }

    public static void setTenantId(String tenantId) {
        tenantIdThreadLocal.set(tenantId);
    }

    public static String getTenantId() {
        return getTenantId(tenantIdThreadLocal.get());
    }

    private static String getTenantId(String tenantId) {
        return tenantId == null ? "0" : tenantId;  //Todo: should throw exception
    }

    private Uni<OidcTenantConfig> createOidcConfig(String tenantId) {
        final OidcTenantConfig tenantConfig = new OidcTenantConfig();
        final Config config = ConfigProvider.getConfig();

        tenantConfig.setTenantId(tenantId);
        tenantConfig.setApplicationType(
                OidcTenantConfig.ApplicationType.valueOf(config.getValue("quarkus.oidc.application-type", String.class).toUpperCase()));

        final OidcTenantConfig.Roles roles = new OidcTenantConfig.Roles();
        roles.setSource(OidcTenantConfig.Roles.Source.accesstoken); //config.getValue("quarkus.oidc.roles.source", OidcTenantConfig.Roles.Source.class));
        tenantConfig.setRoles(roles);

        tenantConfig.setClientId(config.getValue("quarkus.oidc.client-id", String.class));
        tenantConfig.setAuthServerUrl(config.getValue("quarkus.oidc.auth-server-url", String.class) + tenantId);
        return Uni.createFrom().item(tenantConfig);
    }


}
