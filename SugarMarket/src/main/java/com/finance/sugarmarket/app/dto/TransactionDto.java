package com.finance.sugarmarket.app.dto;

import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

public class TransactionDto {

    private CashFlowType cashFlowType;
    private String cashFlowName;
    private BigDecimal transactionAmount;
    private TransactionType transactionType;
    private String eventDateTime;
    private String last4Digit;

    public TransactionDto(CashFlowType cashFlowType, String cashFlowName, BigDecimal transactionAmount,
                          TransactionType transactionType, String eventDate, String last4Digit) {
        super();
        this.cashFlowType = cashFlowType;
        this.cashFlowName = cashFlowName;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.eventDateTime = eventDate;
        this.last4Digit = last4Digit;
    }

    public TransactionDto() {
        super();
    }

    public CashFlowType getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(CashFlowType cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public String getCashFlowName() {
        return cashFlowName;
    }

    public void setCashFlowName(String cashFlowName) {
        this.cashFlowName = cashFlowName;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getLast4Digit() {
        return last4Digit;
    }

    public void setLast4Digit(String last4Digit) {
        this.last4Digit = last4Digit;
    }
}