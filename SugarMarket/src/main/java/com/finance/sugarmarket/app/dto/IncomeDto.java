package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalTime;

import com.finance.sugarmarket.app.enums.CashFlowType;

public class IncomeDto {

	private Long id;
	private CashFlowType incomeType;
	private CashFlowDetailDto cashFlowDetails;
	private BigDecimal amount;
	private Date dateOfEvent;
	private LocalTime timeOfEvent;
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CashFlowType getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(CashFlowType incomeType) {
		this.incomeType = incomeType;
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

	public Date getDateOfEvent() {
		return dateOfEvent;
	}

	public void setDateOfEvent(Date dateOfEvent) {
		this.dateOfEvent = dateOfEvent;
	}

	public LocalTime getTimeOfEvent() {
		return timeOfEvent;
	}

	public void setTimeOfEvent(LocalTime timeOfEvent) {
		this.timeOfEvent = timeOfEvent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
