package com.revolut.vo;

import com.revolut.model.Account;

public class TransferResponse {
	private Account accountFrom;
	private Account accountTo;
	
	public Account getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(Account accountFrom) {
		this.accountFrom = accountFrom;
	}
	public Account getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(Account accountTo) {
		this.accountTo = accountTo;
	}
	
}