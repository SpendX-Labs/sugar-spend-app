package com.finance.sugarmarket.base.dto;

import java.util.List;

import com.finance.sugarmarket.base.enums.Operators;

public class Filter {
	private Operators operator;
	private List<Operands> operands;

	public Operators getOperator() {
		return operator;
	}

	public void setOperator(Operators operator) {
		this.operator = operator;
	}

	public List<Operands> getOperands() {
		return operands;
	}

	public void setOperands(List<Operands> operands) {
		this.operands = operands;
	}

	public Filter() {
		super();
	}

	public Filter(Operators operator, List<Operands> operands) {
		super();
		this.operator = operator;
		this.operands = operands;
	}
}
