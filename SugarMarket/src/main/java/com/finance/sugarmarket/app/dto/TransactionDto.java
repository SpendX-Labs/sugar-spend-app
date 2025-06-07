package com.finance.sugarmarket.app.dto;

import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

public class TransactionDto {
    private Long id;
    private CashFlowType cashFlowType;
    private TransactionType transactionType;
    private CashFlowDetailDto cashFlowDetails;
    private BigDecimal amount;
    private Date transactionDate;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public CashFlowType getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(CashFlowType cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public CashFlowDetailDto getCashFlowDetails() {
        return cashFlowDetails;
    }

    public void setCashFlowDetails(CashFlowDetailDto cashFlowDetails) {
        this.cashFlowDetails = cashFlowDetails;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
