package com.finance.sugarmarket.app.dto;

import java.util.Date;

public class LineChartDto {
	private Double investedAmount;
	private Double currentAmount;
	private Date date;

	public Double getInvestedAmount() {
		return investedAmount;
	}

	public void setInvestedAmount(Double investedAmount) {
		this.investedAmount = investedAmount;
	}

	public Double getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(Double currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LineChartDto() {
		super();
	}

	public LineChartDto(Double investedAmount, Double currentAmount, Date date) {
		super();
		this.investedAmount = investedAmount;
		this.currentAmount = currentAmount;
		this.date = date;
	}

	@Override
	public String toString() {
		return "LineChartDto [investedAmount=" + investedAmount + ", currentAmount=" + currentAmount + ", date=" + date
				+ "]";
	}
	

}
