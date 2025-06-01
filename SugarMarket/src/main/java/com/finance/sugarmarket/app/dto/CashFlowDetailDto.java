package com.finance.sugarmarket.app.dto;

public class CashFlowDetailDto {
	private Long cashFlowId;
	private String cashFlowName;
	private String last4Digit;

	public Long getCashFlowId() {
		return cashFlowId;
	}

	public void setCashFlowId(Long cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

	public String getCashFlowName() {
		return cashFlowName;
	}

	public void setCashFlowName(String cashFlowName) {
		this.cashFlowName = cashFlowName;
	}

	public String getLast4Digit() {
		return last4Digit;
	}

	public void setLast4Digit(String last4Digit) {
		this.last4Digit = last4Digit;
	}

	public CashFlowDetailDto(Long cashFlowId, String cashFlowName, String last4Digit) {
		super();
		this.cashFlowId = cashFlowId;
		this.cashFlowName = cashFlowName;
		this.last4Digit = last4Digit;
	}

	public CashFlowDetailDto() {
		super();
	}

}
