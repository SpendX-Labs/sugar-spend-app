package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

public class ExpenseDto {
    private Integer id;
    private CreditCardDto creditCard;
    private BigDecimal amount;
    private Date expenseDate;
    private LocalTime expenseTime;
    private String reason;

    // Constructors, getters, and setters

    public ExpenseDto() {}

    public ExpenseDto(Integer id, CreditCardDto creditCard, BigDecimal amount, Date expenseDate, LocalTime expenseTime, String reason) {
        this.id = id;
        this.creditCard = creditCard;
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

    public CreditCardDto getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardDto creditCard) {
        this.creditCard = creditCard;
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