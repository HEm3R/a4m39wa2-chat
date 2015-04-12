package org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptionmappers;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.DuplicateKeyValueErrorDto;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.InternalServerErrorDto;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.sql.SQLException;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

    @Override
    public Response toResponse(PersistenceException ex) {
        final Throwable original = ex.getCause();
        if (original != null) {
            if (original instanceof org.hibernate.exception.ConstraintViolationException) {
                final SQLException cause = ((org.hibernate.exception.ConstraintViolationException) original).getSQLException();
                final String field = parseDuplicateKye(cause.getMessage());
                final DuplicateKeyValueErrorDto duplicateKeyValueErrorDto = new DuplicateKeyValueErrorDto(field);
                return Response.status(Response.Status.CONFLICT).entity(duplicateKeyValueErrorDto).type(MediaType.APPLICATION_JSON).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new InternalServerErrorDto()).type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    private String parseDuplicateKye(String message) {
        if (message.endsWith("already exists.")) {
            final int i = message.indexOf("Detail: Key ");
            message = message.substring(i + 12, message.length());
            final StringBuilder sb = new StringBuilder();
            for (char c : message.toCharArray()) {
                if (c == ')' || c == ',' || c == ' ') {
                    break;
                }
                if (c != '(') {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return null;
    }
}

