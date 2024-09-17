package com.finance.sugarmarket.app.model;

import java.math.BigDecimal;
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
@Table(name = "budget_view")
public class BudgetView {

    @Id
    @GeneratedValue
    (strategy = GenerationType.IDENTITY)
    @Column(name = "pk_budget_view_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "fk_credit_card_id", referencedColumnName = "pk_credit_card_id")
    private CreditCard creditCard;
    @Column(name = "budget_month")
    private String budgetMonth;
    @Column(name = "budget_year")
    private Integer budgetYear;
    @Column(name = "actual_amount", nullable = false)
    private BigDecimal actualAmount;
    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "due_date")
    private Date dueDate;
    
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
	public String getBudgetMonth() {
		return budgetMonth;
	}
	public void setBudgetMonth(String budgetMonth) {
		this.budgetMonth = budgetMonth;
	}
	public Integer getBudgetYear() {
		return budgetYear;
	}
	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}
	public BigDecimal getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public BudgetView(Integer id, CreditCard creditCard, String budgetMonth, Integer budgetYear,
			BigDecimal actualAmount, BigDecimal remainingAmount, Date updateDate, Date dueDate) {
		super();
		this.id = id;
		this.creditCard = creditCard;
		this.budgetMonth = budgetMonth;
		this.budgetYear = budgetYear;
		this.actualAmount = actualAmount;
		this.remainingAmount = remainingAmount;
		this.updateDate = updateDate;
		this.dueDate = dueDate;
	}
    
	public BudgetView() {
		super();
	}
}