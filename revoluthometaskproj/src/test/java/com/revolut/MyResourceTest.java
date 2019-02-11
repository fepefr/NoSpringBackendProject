package com.revolut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Stream;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.revolut.model.Account;
import com.revolut.vo.Error;
import com.revolut.vo.TransferRequest;
import com.revolut.vo.TransferResponse;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class MyResourceTest {

	private static final String ACCOUNTS_API = "accounts";
	private HttpServer server;
    private WebTarget target;
	private ArrayList<URI> accountsURI = new ArrayList<URI>();  ;

    @BeforeAll
    public void setUp() {
        server = Main.startServer();
        javax.ws.rs.client.Client c = javax.ws.rs.client.ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @Order(1)
    @ParameterizedTest(name="Run {index}: acctClient={0}, balance={1}")
    @MethodSource("accountsParameters") 
    void testCreateAccounts(String acctClient, Money balance) throws Throwable {
    		Account account = new Account(acctClient,balance);
    		Response resp = target.path(ACCOUNTS_API).request().post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
    		accountsURI.add(resp.getLocation());
    		assertEquals(HttpStatus.CREATED_201.getStatusCode(), resp.getStatusInfo().getStatusCode());
    }
    
    static Stream<Arguments> accountsParameters() throws Throwable {
    	CurrencyUnit cu = Monetary.getCurrency(Locale.getDefault());
        return Stream.of(
            Arguments.of("Client A", Money.of(20.7f,cu)), 
            Arguments.of("Client B", Money.of(100.4f,cu)),
            Arguments.of("Client C", Money.of(170.3f,cu)),
            Arguments.of("Client D", Money.of(240.7f,cu)), 
            Arguments.of("Client E", Money.of(100.4f,cu)),
            Arguments.of("Client F", Money.of(2170.6f,cu)),
            Arguments.of("Client G", Money.of(2320.27f,cu)), 
            Arguments.of("Client H", Money.of(10320.42f,cu)),
            Arguments.of("Client I", Money.of(17320.32f,cu))
        );
    }
    
    @Test
    @Order(2)
    void testRetrieveAccounts() {
    	Response resp = target.path(ACCOUNTS_API).request().get();
     	assertEquals(HttpStatus.OK_200.getStatusCode(), resp.getStatusInfo().getStatusCode());
    }
    
    @Test
    @Order(3)
	void testGetAccountByURI() {
		for (URI uri : accountsURI) {
			Response resp = target.path(uri.getPath()).request().get();
			assertEquals(HttpStatus.OK_200.getStatusCode(), resp.getStatusInfo().getStatusCode());
		}
	}
    
	@ParameterizedTest(name="Run {index}: transfAmout={0}, fee={1}")
	@Order(4)
	@CsvSource({
			"25.5 , 2",
			"243.3 , 11.5",
			"04.4 , 2.1",
			"890.3 , 20.3",
			"500.5 , 4.8",
			"20.9 , 3.2",
			"250.7 , 0"			
	})
    void testTransferAccounts(float transfAmout, float fee) {
    	Random rnd = new Random();
    	Account acctFrom = getRandomAccount(rnd);  
    	Account acctTo = getRandomAccount(rnd);
    	
    	TransferRequest transRequest = new TransferRequest();
    	transRequest.setAcctNumTo(acctTo.getNumber());
    	CurrencyUnit cu = Monetary.getCurrency(Locale.getDefault());
    	transRequest.setAmount(Money.of(transfAmout,cu));
    	transRequest.setFee(Money.of(fee,cu));
    	
    	Response resp = target.path(ACCOUNTS_API).path(acctFrom.getNumber().toString()).
    			request().put(Entity.entity(transRequest, MediaType.APPLICATION_JSON_TYPE));
    	
    	if(HttpStatus.BAD_REQUEST_400.getStatusCode() == resp.getStatusInfo().getStatusCode() ){
    		Error erro = resp.readEntity(Error.class);    		
    		if (erro.getErrorCode() == Error.ERROR_CODE_INSUFFICIENT_FUNDS ||
   				erro.getErrorCode() == Error.ERROR_CODE_SAME_ACCT_TRANSF ||
    		 	erro.getErrorCode() == Error.ERROR_CODE_INVALID_PARAM){
    			assertEquals(HttpStatus.BAD_REQUEST_400.getStatusCode(), resp.getStatusInfo().getStatusCode());
    		}else {
    			assertFalse(true);
    		}
    	}else {
    		assertEquals(HttpStatus.OK_200.getStatusCode(), resp.getStatusInfo().getStatusCode());
    		TransferResponse transfResp = resp.readEntity(TransferResponse.class);
    		Account acctFromUpdated = transfResp.getAccountFrom();
    		Account acctToUpdated = transfResp.getAccountTo();
    		
    		assertEquals(acctFrom.getBalance(), acctFromUpdated.getBalance().add(transRequest.getAmount()).add(transRequest.getFee())); 
    		assertEquals(acctToUpdated.getBalance(), acctTo.getBalance().add(transRequest.getAmount()));
    	}     	
    }

	private Account getRandomAccount(Random rnd) {
		int ri = rnd.nextInt(accountsURI.size());
    	Response resp = target.path(accountsURI.get(ri).getPath()).request().get();
    	return resp.readEntity(Account.class);
	}
    
    @Test
    @Order(5)
    public void testDeleteAccounts() {
		for (URI uri : accountsURI) {
			Response resp = target.path(uri.getPath()).request().delete();
	    	assertEquals(HttpStatus.ACCEPTED_202.getStatusCode(), resp.getStatusInfo().getStatusCode());
		}
	}    
    
    @AfterAll
    public void tearDown() throws Exception {
      server.shutdownNow();
    }    
}