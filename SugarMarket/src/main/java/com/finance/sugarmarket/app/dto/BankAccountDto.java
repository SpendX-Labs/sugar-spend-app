package com.finance.sugarmarket.app.dto;

import com.finance.sugarmarket.app.enums.AccountType;

public class BankAccountDto {

	private Long id;
	private String bankName;
	private AccountType accountType;
	private String last4Digit;
	private String debitCardLast4Digit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getLast4Digit() {
		return last4Digit;
	}

	public void setLast4Digit(String last4Digit) {
		this.last4Digit = last4Digit;
	}

	public String getDebitCardLast4Digit() {
		return debitCardLast4Digit;
	}

	public void setDebitCardLast4Digit(String debitCardLast4Digit) {
		this.debitCardLast4Digit = debitCardLast4Digit;
	}

}
