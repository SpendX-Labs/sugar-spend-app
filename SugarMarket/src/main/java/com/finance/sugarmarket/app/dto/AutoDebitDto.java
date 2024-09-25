package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;
import java.util.List;

public class AutoDebitDto {
	private BigDecimal totalAmount;
	private BigDecimal remainingAmount;
	private List<BudgetViewDto> details;

	public AutoDebitDto() {
		super();
	}

	public AutoDebitDto(BigDecimal totalAmount, BigDecimal remainingAmount, List<BudgetViewDto> details) {
		super();
		this.totalAmount = totalAmount;
		this.remainingAmount = remainingAmount;
		this.details = details;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public List<BudgetViewDto> getDetails() {
		return details;
	}

	public void setDetails(List<BudgetViewDto> details) {
		this.details = details;
	}

}
