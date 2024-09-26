package com.finance.sugarmarket.app.budgetview.dto;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseReportDto {
	private BigDecimal totalAmount;
	private BigDecimal autoDebitAmount;
	private BigDecimal manualSpendAmount;
	private BigDecimal cardSpendAmount;
	private BigDecimal availableAmount;
	private AutoDebitDto remainingAutoDebit;
	private List<TimeBasedSummary> timeBasedSummary;

	public ExpenseReportDto() {
		super();
	}

	public ExpenseReportDto(BigDecimal totalAmount, BigDecimal autoDebitAmount, BigDecimal manualSpendAmount,
			BigDecimal cardSpendAmount, BigDecimal availableAmount, AutoDebitDto remainingAutoDebit,
			List<TimeBasedSummary> timeBasedSummary) {
		super();
		this.totalAmount = totalAmount;
		this.autoDebitAmount = autoDebitAmount;
		this.manualSpendAmount = manualSpendAmount;
		this.cardSpendAmount = cardSpendAmount;
		this.availableAmount = availableAmount;
		this.remainingAutoDebit = remainingAutoDebit;
		this.timeBasedSummary = timeBasedSummary;
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

	public BigDecimal getManualSpendAmount() {
		return manualSpendAmount;
	}

	public void setManualSpendAmount(BigDecimal manualSpendAmount) {
		this.manualSpendAmount = manualSpendAmount;
	}

	public BigDecimal getCardSpendAmount() {
		return cardSpendAmount;
	}

	public void setCardSpendAmount(BigDecimal cardSpendAmount) {
		this.cardSpendAmount = cardSpendAmount;
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

	public List<TimeBasedSummary> getTimeBasedSummary() {
		return timeBasedSummary;
	}

	public void setTimeBasedSummary(List<TimeBasedSummary> timeBasedSummary) {
		this.timeBasedSummary = timeBasedSummary;
	}

}
