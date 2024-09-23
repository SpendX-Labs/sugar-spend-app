package com.finance.sugarmarket.base.enums;

//public enum FilterOperation {
//
//	EQUAL("equal", "="), NOT_EQUAL("notEqual", "<>"), LIKE("like", "LIKE");
//
//	private final String operationName;
//	private final String sqlOperator;
//
//	FilterOperation(String operationName, String sqlOperator) {
//		this.operationName = operationName;
//		this.sqlOperator = sqlOperator;
//	}
//
//	public String getOperationName() {
//		return operationName;
//	}
//
//	public String getSqlOperator() {
//		return sqlOperator;
//	}
//
//	public static FilterOperation fromOperationName(String operationName) {
//		for (FilterOperation operation : values()) {
//			if (operation.operationName.equalsIgnoreCase(operationName)) {
//				return operation;
//			}
//		}
//		throw new IllegalArgumentException("Invalid filter operation: " + operationName);
//	}
//}

public enum FilterOperation {

	EQUAL, NOT_EQUAL, LIKE
}