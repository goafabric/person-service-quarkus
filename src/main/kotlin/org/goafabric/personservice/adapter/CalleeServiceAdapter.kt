package org.goafabric.personservice.adapter

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.faulttolerance.CircuitBreaker
import org.eclipse.microprofile.faulttolerance.Timeout
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@Path("/callees")
@RegisterRestClient
@Timeout
@CircuitBreaker
@RegisterClientHeaders(AdapterConfiguration::class)
@ApplicationScoped
interface CalleeServiceAdapter {
    @GET
    @Path("sayMyName")
    fun sayMyName(@QueryParam("name") name: String): Callee

    @GET
    @Path("sayMyOtherName/{name}")
    fun sayMyOtherName(@PathParam("name") name: String): Callee

    @GET
    @Path("setSleepTime")
    fun setSleepTime(@QueryParam("sleepTime") sleepTime: Long): Callee
}
