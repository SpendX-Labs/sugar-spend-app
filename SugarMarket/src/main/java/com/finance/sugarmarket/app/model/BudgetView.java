package com.finance.sugarmarket.app.model;

import java.math.BigDecimal;
import java.util.Date;

import com.finance.sugarmarket.auth.model.MFUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BudgetView {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "budgetViewId")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private MFUser user;
	@ManyToOne
	@JoinColumn(name = "loanId", referencedColumnName = "loanId")
	private Loan loan;
	@ManyToOne
	@JoinColumn(name = "creditCardId", referencedColumnName = "creditCardId")
	private CreditCard creditCard;
	private String budgetMonth;
	private Integer budgetYear;
	private BigDecimal actualAmount;
	private BigDecimal remainingAmount;
	private Date updateDate;
	private Date dueDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MFUser getUser() {
		return user;
	}

	public void setUser(MFUser user) {
		this.user = user;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
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

	public BudgetView(String budgetMonth, Integer budgetYear, BigDecimal actualAmount,
			BigDecimal remainingAmount, Date updateDate, Date dueDate, CreditCard creditCard, Loan loan) {
		this.budgetMonth = budgetMonth;
		this.budgetYear = budgetYear;
		this.actualAmount = actualAmount;
		this.remainingAmount = remainingAmount;
		this.updateDate = updateDate;
		this.dueDate = dueDate;
		this.creditCard = creditCard;
		this.loan = loan;
	}

}