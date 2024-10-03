package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.finance.sugarmarket.app.enums.LoanType;

public class LoanDto {
	private Long id;
	private Long creditCardId;
	private String creditCardName;
	private CreditCardDto creditCardDTO;
	private String lenderName;
	private String last4Digit;
	private BigDecimal totalAmount;
	private LoanType loanType = LoanType.FLAT;
	private BigDecimal interestRate;
	private boolean noCostEmi;
	private Integer tenure;
	private Integer remainingTenure;
	private Date loanStartDate;
	private Integer emiDateOfMonth;
	private BigDecimal principalAmount;
	private BigDecimal interestAmount;
	private boolean updateLock;
	private BigDecimal remainingAmount;
	private BigDecimal remainingPrincipalAmount;
	private BigDecimal remainingInterestAmount;
	private BigDecimal emiAmount;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCreditCardId() {
		return creditCardId;
	}
	public void setCreditCardId(Long creditCardId) {
		this.creditCardId = creditCardId;
	}
	public String getCreditCardName() {
		return creditCardName;
	}
	public CreditCardDto getCreditCardDTO() {
		return creditCardDTO;
	}
	public void setCreditCardDTO(CreditCardDto creditCardDTO) {
		this.creditCardDTO = creditCardDTO;
	}
	public void setCreditCardName(String creditCardName) {
		this.creditCardName = creditCardName;
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
	public boolean isUpdateLock() {
		return updateLock;
	}
	public void setUpdateLock(boolean updateLock) {
		this.updateLock = updateLock;
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
	public BigDecimal getEmiAmount() {
		return emiAmount;
	}
	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}
}
