package com.revolut.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.javamoney.moneta.Money;
public class TransferRequest {
	@NotNull
	private Long acctNumTo; 
	@NotNull
	@Positive
	private Money amount; 
	@NotNull
	@Positive
	private Money fee;
	
	public Long getAcctNumTo() {
		return acctNumTo;
	}
	public void setAcctNumTo(Long acctNumTo) {
		this.acctNumTo = acctNumTo;
	}
	public Money getAmount() {
		return amount;
	}
	public void setAmount(Money amount) {
		this.amount = amount;
	}
	public Money getFee() {
		return fee;
	}
	public void setFee(Money fee) {
		this.fee = fee;
	}
	

}
