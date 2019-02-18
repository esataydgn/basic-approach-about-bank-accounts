package com.sunhill.app.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sunhill.app.model.enumtype.AccountType;

@JsonInclude(Include.NON_EMPTY)
public class Account {

	private AccountType accountType;
	private Integer userId;
	private String accountId;
	private BigDecimal balance;
	private BigDecimal withdrawalLimit;
	private BigDecimal interestRate;

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getWithdrawalLimit() {
		return withdrawalLimit;
	}

	public void setWithdrawalLimit(BigDecimal withdrawalLimit) {
		this.withdrawalLimit = withdrawalLimit;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
}
