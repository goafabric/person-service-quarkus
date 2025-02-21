package org.goafabric.personservice.extensions;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.core.interception.jaxrs.PostMatchContainerRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

@Provider
@ApplicationScoped
public class HttpInterceptor implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger log = LoggerFactory.getLogger("HttpInterceptor");

    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();
    private static final ThreadLocal<String> userName = new ThreadLocal<>();

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        TenantContext.setContext(request);
        configureLogsAndTracing();
        if (request instanceof PostMatchContainerRequestContext) {
            var method = ((PostMatchContainerRequestContext) request).getResourceMethod().getMethod();
            log.info("{} called for user {} ", method.getDeclaringClass().getName() + "." + method.getName(), TenantContext.getUserName());
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        TenantContext.removeContext();
        MDC.remove("tenantId");
    }

    private static void configureLogsAndTracing() {
        MDC.put("tenantId", TenantContext.getTenantId());
        Span.fromContext(Context.current()).setAttribute("tenant.id", TenantContext.getTenantId());
    }
}
