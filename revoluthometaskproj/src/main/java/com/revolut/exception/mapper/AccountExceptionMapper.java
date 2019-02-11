package com.revolut.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.revolut.exception.AccountException;
import com.revolut.vo.Error;

@Provider
public class AccountExceptionMapper implements ExceptionMapper<AccountException> {

	public Response toResponse(AccountException e) {

		return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
				.entity(new Error(e.getErrorCode(), e.getMessage()))
				.type(MediaType.APPLICATION_JSON).build();
	}

}