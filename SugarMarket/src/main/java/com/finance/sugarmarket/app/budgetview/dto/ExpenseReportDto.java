package com.finance.sugarmarket.app.budgetview.dto;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseReportDto {
	private BigDecimal totalExpense;
	private BigDecimal autoDebitAmount;
	private BigDecimal manualSpendAmount;
	private BigDecimal cardSpendAmount;
	private BigDecimal totalIncome;
	private BigDecimal availableAmount;
	private AutoDebitDto remainingAutoDebit;
	private List<TimeBasedSummary> timeBasedSummary;

	public ExpenseReportDto() {
		super();
	}

	public ExpenseReportDto(BigDecimal totalExpense, BigDecimal autoDebitAmount, BigDecimal manualSpendAmount,
			BigDecimal cardSpendAmount, BigDecimal totalIncome, BigDecimal availableAmount,
			AutoDebitDto remainingAutoDebit, List<TimeBasedSummary> timeBasedSummary) {
		super();
		this.totalExpense = totalExpense;
		this.autoDebitAmount = autoDebitAmount;
		this.manualSpendAmount = manualSpendAmount;
		this.cardSpendAmount = cardSpendAmount;
		this.totalIncome = totalIncome;
		this.availableAmount = availableAmount;
		this.remainingAutoDebit = remainingAutoDebit;
		this.timeBasedSummary = timeBasedSummary;
	}

	public BigDecimal getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(BigDecimal totalExpense) {
		this.totalExpense = totalExpense;
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

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
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
