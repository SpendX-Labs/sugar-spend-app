package com.finance.sugarmarket.base.dto;

import com.finance.sugarmarket.base.enums.FilterOperation;

public class Filter {
	private String column;
    private FilterOperation operation;
    private String value;
    
    public Filter() {
    }
    
    public Filter(String column, FilterOperation operation, String value) {
    	this.column = column;
    	this.operation = operation;
    	this.value = value;
    }
    
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public FilterOperation getOperation() {
		return operation;
	}
	public void setOperation(FilterOperation operation) {
		this.operation = operation;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
