package com.finance.sugarmarket.app.model;

import com.finance.sugarmarket.auth.model.MFUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_credit_card_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pk_mf_user_id")
	private MFUser user;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "credit_card_name")
    private String creditCardName;
    @Column(name = "statement_date")
    private Integer statementDate;
    @Column(name = "due_date")
    private Integer dueDate;
    @Size(min = 4, max = 4, message = "Last 4 digits must be exactly 4 characters")
    @Pattern(regexp = "\\d{4}", message = "Last 4 digits must be numeric")
    @Column(name = "last_4_digit")
    private String last4Digit;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MFUser getUser() {
		return user;
	}
	public void setUser(MFUser user) {
		this.user = user;
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
	public String getLast4Digit() {
		return last4Digit;
	}
	public void setLast4Digit(String last4Digit) {
		this.last4Digit = last4Digit;
	}
	
	public CreditCard(Integer id, MFUser user, String bankName, String creditCardName,
			Integer statementDate, Integer dueDate, String last4Digit) {
		super();
		this.id = id;
		this.user = user;
		this.bankName = bankName;
		this.creditCardName = creditCardName;
		this.statementDate = statementDate;
		this.dueDate = dueDate;
		this.last4Digit = last4Digit;
	}

    public CreditCard() {
    	super();
    }
}
