package com.finance.sugarmarket.app.budgetview.dto;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseReportDto {
    private BigDecimal totalExpense;
    private BigDecimal previousExpense;
    private BigDecimal autoDebitAmount;
    private BigDecimal previousAutoDebitAmount;
    private BigDecimal manualSpendAmount;
    private BigDecimal previousManualSpendAmount;
    private BigDecimal cardSpendAmount;
    private BigDecimal previousCardSpendAmount;
    private BigDecimal totalIncome;
    private BigDecimal previousTotalIncome;
    private BigDecimal availableAmount;
    private BigDecimal previousAvailableAmount;
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

    public ExpenseReportDto(BigDecimal totalExpense, BigDecimal previousExpense, BigDecimal autoDebitAmount,
                            BigDecimal previousAutoDebitAmount, BigDecimal manualSpendAmount, BigDecimal previousManualSpendAmount,
                            BigDecimal cardSpendAmount, BigDecimal previousCardSpendAmount, BigDecimal totalIncome,
                            BigDecimal previousTotalIncome, BigDecimal availableAmount, BigDecimal previousAvailableAmount,
                            AutoDebitDto remainingAutoDebit, List<TimeBasedSummary> timeBasedSummary) {
        super();
        this.totalExpense = totalExpense;
        this.previousExpense = previousExpense;
        this.autoDebitAmount = autoDebitAmount;
        this.previousAutoDebitAmount = previousAutoDebitAmount;
        this.manualSpendAmount = manualSpendAmount;
        this.previousManualSpendAmount = previousManualSpendAmount;
        this.cardSpendAmount = cardSpendAmount;
        this.previousCardSpendAmount = previousCardSpendAmount;
        this.totalIncome = totalIncome;
        this.previousTotalIncome = previousTotalIncome;
        this.availableAmount = availableAmount;
        this.previousAvailableAmount = previousAvailableAmount;
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

    public BigDecimal getPreviousExpense() {
        return previousExpense;
    }

    public void setPreviousExpense(BigDecimal previousExpense) {
        this.previousExpense = previousExpense;
    }

    public BigDecimal getPreviousAutoDebitAmount() {
        return previousAutoDebitAmount;
    }

    public void setPreviousAutoDebitAmount(BigDecimal previousAutoDebitAmount) {
        this.previousAutoDebitAmount = previousAutoDebitAmount;
    }

    public BigDecimal getPreviousManualSpendAmount() {
        return previousManualSpendAmount;
    }

    public void setPreviousManualSpendAmount(BigDecimal previousManualSpendAmount) {
        this.previousManualSpendAmount = previousManualSpendAmount;
    }

    public BigDecimal getPreviousCardSpendAmount() {
        return previousCardSpendAmount;
    }

    public void setPreviousCardSpendAmount(BigDecimal previousCardSpendAmount) {
        this.previousCardSpendAmount = previousCardSpendAmount;
    }

    public BigDecimal getPreviousTotalIncome() {
        return previousTotalIncome;
    }

    public void setPreviousTotalIncome(BigDecimal previousTotalIncome) {
        this.previousTotalIncome = previousTotalIncome;
    }

    public BigDecimal getPreviousAvailableAmount() {
        return previousAvailableAmount;
    }

    public void setPreviousAvailableAmount(BigDecimal previousAvailableAmount) {
        this.previousAvailableAmount = previousAvailableAmount;
    }
}
