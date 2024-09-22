package com.finance.sugarmarket.app.dto;

public class CreditCardDto {

    private Integer id;
    private String bankName;
    private String creditCardName;
    private Integer statementDate;
    private Integer dueDate;
    private Integer last4Digit;

    public CreditCardDto(Integer id, String bankName, String creditCardName, Integer statementDate, Integer dueDate, Integer last4Digit) {
        this.id = id;
        this.bankName = bankName;
        this.creditCardName = creditCardName;
        this.statementDate = statementDate;
        this.dueDate = dueDate;
        this.last4Digit = last4Digit;
    }

    public CreditCardDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public Integer getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(Integer statementDate) {
        this.statementDate = statementDate;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

	public Integer getLast4Digit() {
		return last4Digit;
	}

	public void setLast4Digit(Integer last4Digit) {
		this.last4Digit = last4Digit;
	}
}