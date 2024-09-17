package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;

public class BudgetDto {
	private String dataKey;
	private BigDecimal amount;
	
	public String getDataKey() {
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BudgetDto(String dataKey, BigDecimal amount) {
		super();
		this.dataKey = dataKey;
		this.amount = amount;
	}
	public BudgetDto() {
		super();
	}
}
