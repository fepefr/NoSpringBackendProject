package com.revolut.resource;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revolut.exception.AccountException;
import com.revolut.model.Account;
import com.revolut.service.AccountService;
import com.revolut.vo.TransferRequest;
import com.revolut.vo.TransferResponse;

@Path("accounts")
public class AccountsResource{

	@Inject
	private AccountService accountService;
	
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
	@Path("{acctNumFrom}")
	public Response transfer(@PathParam("acctNumFrom") Long acctNumFrom, TransferRequest transferRequest) throws AccountException{
		validate(transferRequest);
		accountService.transfer(acctNumFrom, transferRequest.getAcctNumTo(), transferRequest.getAmount(), transferRequest.getFee());
		TransferResponse resp = new TransferResponse();
		resp.setAccountFrom(accountService.getAccount(acctNumFrom));
		resp.setAccountTo(accountService.getAccount(transferRequest.getAcctNumTo()));
		return Response.ok().entity(resp).build();
	}

	private void validate(Object request) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(request);
		if(!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
	
	@GET
	@Path("{acctNum}")
	@Produces(MediaType.APPLICATION_JSON)
	public Account get(@PathParam("acctNum") Long acctNum) throws AccountException {
		Account account = accountService.getAccount(acctNum);
	    return account;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Account> getAll() {
	    return accountService.getAccounts();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	 public Response add(Account accountReq) {
		validate(accountReq);
        Long accountNum = accountService.addAccount(accountReq);
        URI uri = URI.create("/accounts/" + accountNum.toString());
        return Response.created(uri).build();
    }

	@Path("{acctNum}")
	@DELETE
	public Response remove(@PathParam("acctNum") Long acctNum) {
		boolean isRemoved = accountService.removeAccount(acctNum);
		Response resp;
		if(isRemoved)
			resp = Response.status(202).build();
		else {
			resp = Response.noContent().build();
		}
		return resp;
	}
}