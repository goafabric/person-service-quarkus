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

@Slf4j
@Provider
public class HttpInterceptor implements ContainerRequestFilter, ContainerResponseFilter {
    @Inject SecurityIdentity securityIdentity;
    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();
    private static final ThreadLocal<String> userName = new ThreadLocal<>();

    public static String getTenantId() { return tenantId.get(); }
    public static String getUserName() { return userName.get(); }
    public static void   setTenantId(String tenantId) { HttpInterceptor.tenantId.set(tenantId); }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        tenantId.set(request.getHeaderString("X-TenantId") != null ? request.getHeaderString("X-TenantId") : "0"); //TODO
        userName.set(request.getHeaderString("X-Auth-Request-Preferred-Username") != null ? request.getHeaderString("X-Auth-Request-Preferred-Username")
                :  securityIdentity.getPrincipal().getName());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        tenantId.remove();
        userName.remove();
    }

}
