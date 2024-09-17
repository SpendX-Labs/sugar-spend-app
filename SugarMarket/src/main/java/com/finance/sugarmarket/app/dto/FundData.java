package com.finance.sugarmarket.app.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.finance.sugarmarket.app.model.OrderDetail;

public class FundData {
	private Integer id;
	private String fundName;
	private Double investedAmount;
	private Double currentAmount;
	private Double returnPercentage;
	private Double returnAmount;
	private Double xirrValue;
	private Double totalUnits;
	private Double currentNav;
	private Double day1ChangeAmount;
	private Double day1Change;
	private Double week1Change;
	private Double month1Change;
	private Double month3Change;
	private Double month6Change;
	private Double year1Change;
	private Double year3Change;
	private Double year5Change;
	private Double allTimeChange;
	public Map<String, String> meta;
	public List<OrderDetail> orderDetails;
	public Date updatedDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
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
	public Double getReturnPercentage() {
		return returnPercentage;
	}
	public void setReturnPercentage(Double returnPercentage) {
		this.returnPercentage = returnPercentage;
	}
	public Double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public Double getXirrValue() {
		return xirrValue;
	}
	public void setXirrValue(Double xirrValue) {
		this.xirrValue = xirrValue;
	}
	public Double getTotalUnits() {
		return totalUnits;
	}
	public void setTotalUnits(Double totalUnits) {
		this.totalUnits = totalUnits;
	}
	public Double getCurrentNav() {
		return currentNav;
	}
	public void setCurrentNav(Double currentNav) {
		this.currentNav = currentNav;
	}
	public Double getDay1ChangeAmount() {
		return day1ChangeAmount;
	}
	public void setDay1ChangeAmount(Double day1ChangeAmount) {
		this.day1ChangeAmount = day1ChangeAmount;
	}
	public Double getDay1Change() {
		return day1Change;
	}
	public void setDay1Change(Double day1Change) {
		this.day1Change = day1Change;
	}
	public Double getWeek1Change() {
		return week1Change;
	}
	public void setWeek1Change(Double week1Change) {
		this.week1Change = week1Change;
	}
	public Double getMonth1Change() {
		return month1Change;
	}
	public void setMonth1Change(Double month1Change) {
		this.month1Change = month1Change;
	}
	public Double getMonth3Change() {
		return month3Change;
	}
	public void setMonth3Change(Double month3Change) {
		this.month3Change = month3Change;
	}
	public Double getMonth6Change() {
		return month6Change;
	}
	public void setMonth6Change(Double month6Change) {
		this.month6Change = month6Change;
	}
	public Double getYear1Change() {
		return year1Change;
	}
	public void setYear1Change(Double year1Change) {
		this.year1Change = year1Change;
	}
	public Double getYear3Change() {
		return year3Change;
	}
	public void setYear3Change(Double year3Change) {
		this.year3Change = year3Change;
	}
	public Double getYear5Change() {
		return year5Change;
	}
	public void setYear5Change(Double year5Change) {
		this.year5Change = year5Change;
	}
	public Double getAllTimeChange() {
		return allTimeChange;
	}
	public void setAllTimeChange(Double allTimeChange) {
		this.allTimeChange = allTimeChange;
	}
	public Map<String, String> getMeta() {
		return meta;
	}
	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
