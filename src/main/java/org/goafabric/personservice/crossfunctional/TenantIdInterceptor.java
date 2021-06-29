package org.goafabric.personservice.crossfunctional;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Slf4j
@Provider
public class TenantIdInterceptor implements ContainerRequestFilter, ContainerResponseFilter {
    private static final ThreadLocal<String> tenantIdThreadLocal = new ThreadLocal<>();

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        final String tenantId = request.getHeaderString("X-TenantId");
        log.debug("#interceptor got tenant id {}", tenantId);
        tenantIdThreadLocal.set(request.getHeaderString("X-TenantId"));
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        tenantIdThreadLocal.remove();
    }

    public static String getTenantId() {
        final String tenantId = tenantIdThreadLocal.get();
        return tenantId == null ? "0" : tenantId;  //Todo: should throw exception
    }
}
