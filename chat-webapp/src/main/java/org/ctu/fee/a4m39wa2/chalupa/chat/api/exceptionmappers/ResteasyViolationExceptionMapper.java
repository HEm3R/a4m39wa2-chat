package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptionmappers;

import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.ValidationErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResteasyViolationExceptionMapper implements ExceptionMapper<ResteasyViolationException> {

    @Override
    public Response toResponse(ResteasyViolationException ex) {
        final ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        for (ResteasyConstraintViolation cv : ex.getParameterViolations()) {
            validationErrorDto.addInvalidField(new ValidationErrorDto.InvalidField(parseField(cv.getPath()), cv.getMessage()));
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(validationErrorDto).type(MediaType.APPLICATION_JSON).build();
    }

    private String parseField(String path) {
        final int i = path.lastIndexOf('.');
        if (i > 0) {
            return path.substring(i + 1, path.length());
        }
        return path;
    }
}