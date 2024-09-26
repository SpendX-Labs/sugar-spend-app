package com.finance.sugarmarket.app.budgetview.dto;

import java.math.BigDecimal;

public class BudgetViewDto {
	private String lender;
	private String dueDate;
	private BigDecimal dueAmount;
	private BigDecimal remainingDueAmount;

	public String getLender() {
		return lender;
	}

	public void setLender(String lender) {
		this.lender = lender;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}

	public BigDecimal getRemainingDueAmount() {
		return remainingDueAmount;
	}

	public void setRemainingDueAmount(BigDecimal remainingDueAmount) {
		this.remainingDueAmount = remainingDueAmount;
	}

}
