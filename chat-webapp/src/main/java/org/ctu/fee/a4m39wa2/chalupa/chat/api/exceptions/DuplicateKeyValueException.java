package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.DuplicateKeyValueErrorDto;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DuplicateKeyValueException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public DuplicateKeyValueException(String field) {
        super(Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(new DuplicateKeyValueErrorDto(field)).build());
    }
}