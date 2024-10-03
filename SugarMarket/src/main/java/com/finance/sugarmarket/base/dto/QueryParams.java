package com.finance.sugarmarket.base.dto;

import java.util.List;

public class QueryParams {
	private Integer page = 0;
	private Integer size = 10;
	private String sortName;
	private String orderby = "asc";
	private List<Operands> filters;

	// Getters and setters

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public List<Operands> getFilters() {
		return filters;
	}

	public void setFilters(List<Operands> filters) {
		this.filters = filters;
	}
}