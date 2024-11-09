package com.finance.sugarmarket.app.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

import com.finance.sugarmarket.app.enums.CashFlowType;
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

@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expenseId")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private MFUser user;

	@Enumerated(EnumType.STRING)
	private CashFlowType expenseType;

	@ManyToOne
	@JoinColumn(name = "bankAccountId", referencedColumnName = "bankAccountId")
	private BankAccount bankAccount;

	@ManyToOne
	@JoinColumn(name = "creditCardId", referencedColumnName = "creditCardId")
	private CreditCard creditCard;

	private BigDecimal amount;

	private Date expenseDate;

	private LocalTime expenseTime;

	private String reason;

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

	public CashFlowType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(CashFlowType expenseType) {
		this.expenseType = expenseType;
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

	public Date getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}

	public LocalTime getExpenseTime() {
		return expenseTime;
	}

	public void setExpenseTime(LocalTime expenseTime) {
		this.expenseTime = expenseTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Expense() {
		super();
	}

	public Expense(Expense expense) {
		super();
		this.user = expense.getUser();
		this.expenseType = expense.getExpenseType();
		this.bankAccount = expense.getBankAccount();
		this.creditCard = expense.getCreditCard();
		this.amount = expense.getAmount();
		this.expenseDate = expense.getExpenseDate();
		this.expenseTime = expense.getExpenseTime();
		this.reason = expense.getReason();
	}

}
