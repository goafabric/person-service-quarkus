package org.goafabric.personservice.adapter;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@Path("/callees")
@RegisterRestClient
@Timeout
@CircuitBreaker
@RegisterClientHeaders(AdapterConfiguration.class)
public interface CalleeServiceAdapter {
    @GET
    @Path("sayMyName")
    Callee sayMyName(@QueryParam("name") String name);

    @GET
    @Path("sayMyOtherName/{name}")
    Callee sayMyOtherName(@PathParam("name") String name);

    @GET
    @Path("setSleepTime")
    Callee setSleepTime(@QueryParam("sleepTime") Long sleepTime);
}
