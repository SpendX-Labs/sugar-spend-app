package com.finance.sugarmarket.app.model;

import java.math.BigDecimal;
import java.util.Date;

import com.finance.sugarmarket.app.enums.LoanType;
import com.finance.sugarmarket.auth.model.MFUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loanId")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "userId")
	private MFUser user;

	@ManyToOne
	@JoinColumn(name = "creditCardId")
	private CreditCard creditCard;

	private String lenderName;

	private String last4Digit;

	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	private LoanType loanType;

	private BigDecimal interestRate;

	private boolean noCostEmi = false;

	private BigDecimal principalAmount;

	private BigDecimal interestAmount;

	private BigDecimal remainingAmount;

	private BigDecimal remainingPrincipalAmount;

	private BigDecimal remainingInterestAmount;

	private Integer tenure;

	private Integer remainingTenure;

	private Date loanStartDate;

	private Integer emiDateOfMonth;

	private BigDecimal emiAmount;

	private boolean updateLock = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getLenderName() {
		return lenderName;
	}

	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}

	public String getLast4Digit() {
		return last4Digit;
	}

	public void setLast4Digit(String last4Digit) {
		this.last4Digit = last4Digit;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public boolean isNoCostEmi() {
		return noCostEmi;
	}

	public void setNoCostEmi(boolean noCostEmi) {
		this.noCostEmi = noCostEmi;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public BigDecimal getRemainingPrincipalAmount() {
		return remainingPrincipalAmount;
	}

	public void setRemainingPrincipalAmount(BigDecimal remainingPrincipalAmount) {
		this.remainingPrincipalAmount = remainingPrincipalAmount;
	}

	public BigDecimal getRemainingInterestAmount() {
		return remainingInterestAmount;
	}

	public void setRemainingInterestAmount(BigDecimal remainingInterestAmount) {
		this.remainingInterestAmount = remainingInterestAmount;
	}

	public Integer getTenure() {
		return tenure;
	}

	public void setTenure(Integer tenure) {
		this.tenure = tenure;
	}

	public Integer getRemainingTenure() {
		return remainingTenure;
	}

	public void setRemainingTenure(Integer remainingTenure) {
		this.remainingTenure = remainingTenure;
	}

	public Date getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(Date loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public Integer getEmiDateOfMonth() {
		return emiDateOfMonth;
	}

	public void setEmiDateOfMonth(Integer emiDateOfMonth) {
		this.emiDateOfMonth = emiDateOfMonth;
	}

	public BigDecimal getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}

	public boolean isUpdateLock() {
		return updateLock;
	}

	public void setUpdateLock(boolean updateLock) {
		this.updateLock = updateLock;
	}

}
