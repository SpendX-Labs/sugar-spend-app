package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

public class ExpenseDto {
	private Integer id;
	private Integer crediCardId;
	private String creditCardName;
	private BigDecimal amount;
	private Date expenseDate;
	private LocalTime expenseTime;
	private String reason;

	public ExpenseDto() {
	}

	public ExpenseDto(Integer id, Integer crediCardId, String creditCardName, BigDecimal amount, Date expenseDate,
			LocalTime expenseTime, String reason) {
		this.id = id;
		this.crediCardId = crediCardId;
		this.creditCardName = creditCardName;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.expenseTime = expenseTime;
		this.reason = reason;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCrediCardId() {
		return crediCardId;
	}

	public void setCrediCardId(Integer crediCardId) {
		this.crediCardId = crediCardId;
	}

	public String getCreditCardName() {
		return creditCardName;
	}

	public void setCreditCardName(String creditCardName) {
		this.creditCardName = creditCardName;
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