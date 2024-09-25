package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;

public class ExpenseReportDto {
	private BigDecimal totalAmount;
	private BigDecimal autoDebitAmount;
	private BigDecimal directSpendAmount;
	private BigDecimal availableAmount;
	private AutoDebitDto remainingAutoDebit;

	public ExpenseReportDto() {
		super();
	}

	public ExpenseReportDto(BigDecimal totalAmount, BigDecimal autoDebitAmount, BigDecimal directSpendAmount,
			BigDecimal availableAmount, AutoDebitDto remainingAutoDebit) {
		super();
		this.totalAmount = totalAmount;
		this.autoDebitAmount = autoDebitAmount;
		this.directSpendAmount = directSpendAmount;
		this.availableAmount = availableAmount;
		this.remainingAutoDebit = remainingAutoDebit;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getAutoDebitAmount() {
		return autoDebitAmount;
	}

	public void setAutoDebitAmount(BigDecimal autoDebitAmount) {
		this.autoDebitAmount = autoDebitAmount;
	}

	public BigDecimal getDirectSpendAmount() {
		return directSpendAmount;
	}

	public void setDirectSpendAmount(BigDecimal directSpendAmount) {
		this.directSpendAmount = directSpendAmount;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public AutoDebitDto getRemainingAutoDebit() {
		return remainingAutoDebit;
	}

	public void setRemainingAutoDebit(AutoDebitDto remainingAutoDebit) {
		this.remainingAutoDebit = remainingAutoDebit;
	}

}
