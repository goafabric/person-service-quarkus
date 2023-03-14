package org.goafabric.personservice.crossfunctional;

import io.quarkus.security.identity.SecurityIdentity;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class HttpInterceptor implements ContainerRequestFilter, ContainerResponseFilter {
    private final SecurityIdentity securityIdentity;

    public HttpInterceptor(SecurityIdentity securityIdentity) {
        this.securityIdentity = securityIdentity;
    }

    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();
    private static final ThreadLocal<String> userName = new ThreadLocal<>();

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        tenantId.set(request.getHeaderString(request.getHeaderString("X-TenantId")));
        userName.set(request.getHeaderString("X-Auth-Request-Preferred-Username") != null ? request.getHeaderString("X-Auth-Request-Preferred-Username")
                :  securityIdentity != null ? securityIdentity.getPrincipal().getName() : "");
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        tenantId.remove();
        userName.remove();
    }

    public static void setTenantId(String tenantId) {
        HttpInterceptor.tenantId.set(tenantId);
    }

    public static String getTenantId() {
        return tenantId.get() != null ? tenantId.get() : "0"; //tdo
    }

    public static String getUserName() {
        return userName.get();
    }

}
