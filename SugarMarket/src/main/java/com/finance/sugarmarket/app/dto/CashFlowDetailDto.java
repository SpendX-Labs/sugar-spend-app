package com.finance.sugarmarket.app.dto;

public class CashFlowDetailDto {
	private Long cashFlowId;
	private String cashFlowName;

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

	public CashFlowDetailDto(Long cashFlowId, String cashFlowName) {
		super();
		this.cashFlowId = cashFlowId;
		this.cashFlowName = cashFlowName;
	}

	public CashFlowDetailDto() {
		super();
	}

}
