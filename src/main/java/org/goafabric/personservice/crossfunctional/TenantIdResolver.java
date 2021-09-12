package org.goafabric.personservice.crossfunctional;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Slf4j
public class TenantIdResolver implements io.quarkus.oidc.TenantResolver {

    @Override
    public String resolve(RoutingContext context) {
        log.info("#Tenantresolver got: " + context.request().getHeader("X-TenantId"));
        return context.request().getHeader("X-TenantId");
    }
}
