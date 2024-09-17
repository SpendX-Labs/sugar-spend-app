package com.finance.sugarmarket.app.model;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_expense_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "fk_credit_card_id", referencedColumnName = "pk_credit_card_id")
    private CreditCard creditCard;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "expense_date")
    private Date expenseDate;
    @Column(name = "expense_time")
    private LocalTime expenseTime;
    @Column(name = "reason")
    private String reason;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
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
	
	public Expense(Integer id, CreditCard creditCard, BigDecimal amount, Date expenseDate, LocalTime expenseTime,
			String reason) {
		super();
		this.id = id;
		this.creditCard = creditCard;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.expenseTime = expenseTime;
		this.reason = reason;
	}
    
	public Expense() {
		super();
	}
}
