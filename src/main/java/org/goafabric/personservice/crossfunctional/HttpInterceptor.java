package org.goafabric.personservice.crossfunctional;

import io.quarkus.security.identity.SecurityIdentity;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Provider
public class HttpInterceptor implements ContainerRequestFilter, ContainerResponseFilter {
    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();
    private static final ThreadLocal<String> userName = new ThreadLocal<>();
    @Inject SecurityIdentity securityIdentity;

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        setTenantId(request.getHeaderString("X-TenantId"));
        tenantId.set(request.getHeaderString("X-TenantId") != null ? request.getHeaderString("X-TenantId") : "0"); //TODO
        userName.set(request.getHeaderString("X-Auth-Request-Preferred-Username") != null ? request.getHeaderString("X-Auth-Request-Preferred-Username")
                :  securityIdentity.getPrincipal().getName());
        logHeaders(request.getHeaders().entrySet());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        tenantId.remove();
        userName.remove();
    }

    private void logHeaders(Set<Map.Entry<String, List<String>>> headers) {
        if (!log.isDebugEnabled()) { return; }
        for (Map.Entry<String, List<String>> entry : headers) {
            log.info(entry.getKey() + " : " + entry.getValue());
        }
    }     

    public static String getTenantId() { return tenantId.get(); }

    public static String getUserName() { return userName.get(); }

    public static void setTenantId(String tenantId) { HttpInterceptor.tenantId.set(tenantId); }
}
