package org.goafabric.personservice.extensions

import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider
import org.jboss.resteasy.core.interception.jaxrs.PostMatchContainerRequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.io.IOException

@Provider
@ApplicationScoped
class HttpInterceptor : ContainerRequestFilter, ContainerResponseFilter {
    @Throws(IOException::class)
    override fun filter(request: ContainerRequestContext) {
        UserContext.setContext(request)
        configureLogsAndTracing()
        if (request is PostMatchContainerRequestContext) {
            val method = request.getResourceMethod().getMethod()
            log.info(
                "{} called for user {} ",
                method.getDeclaringClass().getName() + "." + method.getName(),
                UserContext.userName
            )
        }
    }

    @Throws(IOException::class)
    override fun filter(
        containerRequestContext: ContainerRequestContext?,
        containerResponseContext: ContainerResponseContext?
    ) {
        UserContext.removeContext()
        MDC.remove("tenantId")
    }

    private val log: Logger = LoggerFactory.getLogger("HttpInterceptor")

    private fun configureLogsAndTracing() {
        MDC.put("tenantId", UserContext.tenantId)
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.tenantId)
    }

}
