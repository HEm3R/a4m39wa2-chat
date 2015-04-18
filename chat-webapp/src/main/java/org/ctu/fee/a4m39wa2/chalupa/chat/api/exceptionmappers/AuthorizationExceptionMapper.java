package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptionmappers;

import org.ctu.fee.a4m39wa2.chalupa.chat.security.AuthorizationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthorizationExceptionMapper implements ExceptionMapper<AuthorizationException> {

    @Override
    public Response toResponse(AuthorizationException ex) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
