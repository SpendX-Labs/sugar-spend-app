package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

import com.finance.sugarmarket.app.enums.CashFlowType;

public class ExpenseDto {
	private Long id;
	private CashFlowType expenseType;
	private Long crediCardId;
	private String creditCardName;
	private Long bankAccountId;
	private String bankAccountName;
	private BigDecimal amount;
	private Date expenseDate;
	private LocalTime expenseTime;
	private String reason;

	public ExpenseDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CashFlowType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(CashFlowType expenseType) {
		this.expenseType = expenseType;
	}

	public Long getCrediCardId() {
		return crediCardId;
	}

	public void setCrediCardId(Long crediCardId) {
		this.crediCardId = crediCardId;
	}

	public String getCreditCardName() {
		return creditCardName;
	}

	public void setCreditCardName(String creditCardName) {
		this.creditCardName = creditCardName;
	}

	public Long getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
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
}