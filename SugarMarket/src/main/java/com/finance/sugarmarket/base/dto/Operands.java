package com.finance.sugarmarket.base.dto;

import com.finance.sugarmarket.base.enums.Operators;

public class Operands {
	private String column;
    private Operators operation;
    private String value;
    
    public Operands() {
    }
    
    public Operands(String column, Operators operation, String value) {
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
	public Operators getOperation() {
		return operation;
	}
	public void setOperation(Operators operation) {
		this.operation = operation;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
