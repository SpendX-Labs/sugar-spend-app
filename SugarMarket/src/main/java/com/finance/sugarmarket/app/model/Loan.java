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
import jakarta.persistence.Table;

@Entity
@Table(name = "loan")
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_loan_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "fk_user_id")
	private MFUser user;

	@ManyToOne
	@JoinColumn(name = "fk_credit_card_id")
	private CreditCard creditCard;

	@Column(name = "lender_name", length = 255)
	private String lenderName;

	@Column(name = "last_4_digit")
	private Integer last4Digit;

	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "loan_type")
	private LoanType loanType;

	@Column(name = "interest_rate")
	private BigDecimal interestRate;

	@Column(name = "no_cost_emi")
	private boolean noCostEmi = false;

	@Column(name = "principal_amount")
	private BigDecimal principalAmount;

	@Column(name = "interest_amount")
	private BigDecimal interestAmount;

	@Column(name = "remaining_amount")
	private BigDecimal remainingAmount;

	@Column(name = "remaining_principal_amount")
	private BigDecimal remainingPrincipalAmount;

	@Column(name = "remaining__interest_amount")
	private BigDecimal remainingInterestAmount;

	@Column(name = "tenure")
	private Integer tenure;

	@Column(name = "remaining_tenure")
	private Integer remainingTenure;

	@Column(name = "loan_start_date")
	private Date loanStartDate;
	
	@Column(name = "emi_date_of_month")
	private Integer emiDateOfMonth;
	
	@Column(name = "emi_amount")
	private BigDecimal emiAmount;
	
	@Column(name = "update_lock")
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

	public Integer getLast4Digit() {
		return last4Digit;
	}

	public void setLast4Digit(Integer last4Digit) {
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
