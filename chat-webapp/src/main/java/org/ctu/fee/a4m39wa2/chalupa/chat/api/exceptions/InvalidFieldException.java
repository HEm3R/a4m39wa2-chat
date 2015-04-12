package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.ValidationErrorDto;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InvalidFieldException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public InvalidFieldException(String field, String message) {
        super(
                Response
                        .status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ValidationErrorDto(new ValidationErrorDto.InvalidField(field, message)))
                        .build()
        );
    }
}