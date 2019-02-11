package com.revolut.service;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.SplittableRandom;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.javamoney.moneta.Money;

import com.revolut.exception.AccountException;
import com.revolut.model.Account;
import com.revolut.model.Bank;
import com.revolut.vo.Error;

@Singleton
public class AccountService {

	@Inject
	private Bank bank;

	public void transfer(Long acctNumFrom, Long AcctNumTo, Money amount, Money fee) throws AccountException {
		Account accountFrom = getAccount(acctNumFrom) ;
		Account accountTo = getAccount(AcctNumTo);
		if (accountFrom.equals(accountTo))
			throw new AccountException(Error.ERROR_CODE_SAME_ACCT_TRANSF, Error.ERROR_MSG_INVALID_TRANSFERECES_TO_THE_SAME_ACCOUNT);
		withdraw(accountFrom, amount, fee);
		deposit(accountTo, amount);
	}
	
	private void deposit(Account account, Money amount) {
		Money balance = account.getBalance().add(amount);
		account.setBalance(balance);
	}

	private void withdraw(Account account, Money amount, Money fee) throws AccountException {
		amount = amount.add(fee);
		Money balance = account.getBalance();
		if (amount.isGreaterThan(balance)) {
			throw new AccountException(Error.ERROR_CODE_INSUFFICIENT_FUNDS, Error.ERROR_MSG_BALANCE_IS_NOT_SUFFICIENT + balance);
		}
		balance = balance.subtract(amount);
		account.setBalance(balance);
	}
	

	public Account getAccount(Long acctNum) throws AccountException {
		Account account = bank.getAccount(acctNum);
		if (account == null) {
			throw new AccountException(Error.ERROR_CODE_ACCOUNT_NOT_FOUND, MessageFormat.format(Error.ERROR_MSG_ACCOUNT_NOT_FOUND, acctNum));
		}
		return account;
	}

	public Collection<Account> getAccounts() {
		return bank.getAccounts().values();
	}

	public Long addAccount(Account account) {
		Long number= new SplittableRandom().nextLong(0, 1_00001);
		account.setNumber(number);
		bank.addAccount(account);
		return number;
	}

	public boolean removeAccount(Long acctNum) {
		return bank.removeAccount(acctNum);
	}

}
