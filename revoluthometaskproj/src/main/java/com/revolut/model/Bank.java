package com.revolut.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class Bank {
	private Map<Long, Account> accounts;

	public Bank() {
		super();
		this.accounts = new LinkedHashMap<Long, Account>();
	}

	public Map<Long, Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(Map<Long, Account> accounts) {
		this.accounts = accounts;
	}
	
    public void addAccount(Account account) {
    	this.accounts.put(account.getNumber(),account);
    }
    
	public Account getAccount(Long accountNum) {
		return this.accounts.get(accountNum);
	}
	
	public boolean removeAccount(Long accountNum) {
		return (this.accounts.remove(accountNum)==null)?false:true;
	}
}