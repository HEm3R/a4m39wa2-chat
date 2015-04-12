package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.NotFoundErrorDto;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;

public class RequestedEntityNotFoundException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public RequestedEntityNotFoundException(URI uri) {
        super(Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new NotFoundErrorDto(uri.toString())).build());
    }
}
