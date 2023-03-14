package org.goafabric.personservice.crossfunctional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response toResponse(Exception e) {
        final Response.Status status;
        if (e instanceof IllegalArgumentException) {
            status = Response.Status.PRECONDITION_FAILED;
        } else if (e instanceof IllegalStateException) {
            status = Response.Status.PRECONDITION_FAILED;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        
        log.error(e.getMessage(), e);
        return Response.status(status)
                .entity("an error occured: " + e.getMessage()).build();
    }
}
