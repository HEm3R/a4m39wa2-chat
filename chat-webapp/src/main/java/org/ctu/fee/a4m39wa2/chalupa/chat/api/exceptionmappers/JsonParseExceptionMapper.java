package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptionmappers;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.JsonParseErrorDto;

import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Override
    public Response toResponse(JsonParseException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new JsonParseErrorDto()).type(MediaType.APPLICATION_JSON).build();
    }
}
