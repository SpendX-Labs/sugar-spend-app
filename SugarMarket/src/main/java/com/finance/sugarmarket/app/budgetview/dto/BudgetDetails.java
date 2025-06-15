package com.finance.sugarmarket.app.budgetview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class BudgetDetails implements Serializable {
    @JsonProperty("e")
    private BigDecimal expense;

    @JsonProperty("ada")
    private BigDecimal autoDebitAmount;

    @JsonProperty("msa")
    private BigDecimal manualSpendAmount;

    @JsonProperty("csa")
    private BigDecimal cardSpendAmount;

    @JsonProperty("ti")
    private BigDecimal totalIncome;

    @JsonProperty("aa")
    private BigDecimal availableAmount;

    @JsonProperty("ata")
    private BigDecimal autoDebitTotalAmount;

    @JsonProperty("ara")
    private BigDecimal autoDebitRemainingAmount;

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
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

    public BigDecimal getAutoDebitTotalAmount() {
        return autoDebitTotalAmount;
    }

    public void setAutoDebitTotalAmount(BigDecimal autoDebitTotalAmount) {
        this.autoDebitTotalAmount = autoDebitTotalAmount;
    }

    public BigDecimal getAutoDebitRemainingAmount() {
        return autoDebitRemainingAmount;
    }

    public void setAutoDebitRemainingAmount(BigDecimal autoDebitRemainingAmount) {
        this.autoDebitRemainingAmount = autoDebitRemainingAmount;
    }
}