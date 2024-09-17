package com.finance.sugarmarket.app.dto;

import java.util.List;

public class MutualFundPortfolio {
	private Double currentAmount;
	private Double investedAmount;
	private Double returnAmount;
	private Double returnPercentage;
	private Double xirr;
	private Double dayChange;
	private Double dayChangePercentage;
	private List<FundData> fundData;
	
	public MutualFundPortfolio() {
		super();
	}
	
	public MutualFundPortfolio(Double currentAmount, Double investedAmount, Double returnAmount,
			Double returnPercentage, Double xirr, Double dayChange, Double dayChangePercentage, List<FundData> fundData) {
		super();
		this.currentAmount = currentAmount;
		this.investedAmount = investedAmount;
		this.returnAmount = returnAmount;
		this.returnPercentage = returnPercentage;
		this.xirr = xirr;
		this.dayChange = dayChange;
		this.dayChangePercentage = dayChangePercentage;
		this.fundData = fundData;
	}
	
	public Double getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Double currentAmount) {
		this.currentAmount = currentAmount;
	}
	public Double getInvestedAmount() {
		return investedAmount;
	}
	public void setInvestedAmount(Double investedAmount) {
		this.investedAmount = investedAmount;
	}
	public Double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public Double getReturnPercentage() {
		return returnPercentage;
	}
	public void setReturnPercentage(Double returnPercentage) {
		this.returnPercentage = returnPercentage;
	}
	public Double getXirr() {
		return xirr;
	}
	public void setXirr(Double xirr) {
		this.xirr = xirr;
	}
	public Double getDayChange() {
		return dayChange;
	}
	public void setDayChange(Double dayChange) {
		this.dayChange = dayChange;
	}
	public Double getDayChangePercentage() {
		return dayChangePercentage;
	}

	public void setDayChangePercentage(Double dayChangePercentage) {
		this.dayChangePercentage = dayChangePercentage;
	}

	public List<FundData> getFundData() {
		return fundData;
	}
	public void setFundData(List<FundData> fundData) {
		this.fundData = fundData;
	}
	
}
