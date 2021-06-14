package org.goafabric.personservice.adapter;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/callees")
@RegisterRestClient
public interface CalleeAdapter {

    @GET
    @Path("isAlive")
    //Produces APPLICATION_JSON not allowed here
    Boolean isAlive();
}
