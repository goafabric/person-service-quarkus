package org.goafabric.personservice.adapter;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/callees")
@RegisterRestClient
@Singleton
@Timeout
@CircuitBreaker()
public interface CalleeAdapter {

    @GET
    @Path("isAlive")
    //Produces APPLICATION_JSON not allowed here
    Boolean isAlive();
}
