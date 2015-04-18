package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptionmappers;

import org.ctu.fee.a4m39wa2.chalupa.chat.security.AuthenticationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

    @Override
    public Response toResponse(AuthenticationException ex) {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic realm=\"secured\"")
                .build();
    }
}
