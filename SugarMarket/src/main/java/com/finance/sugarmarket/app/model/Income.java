package com.finance.sugarmarket.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.auth.model.MFUser;

@Entity
public class Income {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "incomeId")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private MFUser user;

	@Enumerated(EnumType.STRING)
	private CashFlowType incomeType;

	@ManyToOne
	@JoinColumn(name = "bankAccountId", referencedColumnName = "bankAccountId")
	private BankAccount bankAccount;

	@ManyToOne
	@JoinColumn(name = "creditCardId", referencedColumnName = "creditCardId")
	private CreditCard creditCard;

	private BigDecimal amount;

	private Date dateOfEvent;

	private LocalTime timeOfEvent;

	private String message;

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

	public CashFlowType getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(CashFlowType incomeType) {
		this.incomeType = incomeType;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDateOfEvent() {
		return dateOfEvent;
	}

	public void setDateOfEvent(Date dateOfEvent) {
		this.dateOfEvent = dateOfEvent;
	}

	public LocalTime getTimeOfEvent() {
		return timeOfEvent;
	}

	public void setTimeOfEvent(LocalTime timeOfEvent) {
		this.timeOfEvent = timeOfEvent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
