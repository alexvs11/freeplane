package org.freeplane.plugin.script.proxy;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.glassfish.grizzly.utils.Exceptions;

@Provider
public class ServerExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        // FIXME: move to a logger
        return Response.status(500).entity(Exceptions.getStackTraceAsString(ex)).type("text/plain")
                .build();
    }
}
