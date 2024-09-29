package com.finance.sugarmarket.app.model;

import com.finance.sugarmarket.app.enums.AccountType;
import com.finance.sugarmarket.auth.model.MFUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bankAccountId")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private MFUser user;

	private String bankName;

	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	@Size(min = 4, max = 4, message = "Last 4 digits must be exactly 4 characters")
	@Pattern(regexp = "\\d{4}", message = "Last 4 digits must be numeric")
	private String last4Digit;

	private String debitCardLast4Digit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MFUser getUser() {
		return user;
	}

	public void setUser(MFUser user) {
		this.user = user;
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