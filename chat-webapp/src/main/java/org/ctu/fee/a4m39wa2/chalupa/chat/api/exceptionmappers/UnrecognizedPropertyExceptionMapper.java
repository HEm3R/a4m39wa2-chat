package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptionmappers;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.UnknownFieldErrorDto;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

    @Override
    public Response toResponse(UnrecognizedPropertyException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new UnknownFieldErrorDto(e.getPropertyName()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
