package com.revolut.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.revolut.vo.Error;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception> {

    public Response toResponse(Exception e) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity(new Error(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode(), e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
