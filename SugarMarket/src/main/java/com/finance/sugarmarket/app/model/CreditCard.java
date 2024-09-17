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
	
	public CreditCard(Integer id, MFUser user, String bankName, String creditCardName,
			Integer statementDate, Integer dueDate) {
		super();
		this.id = id;
		this.user = user;
		this.bankName = bankName;
		this.creditCardName = creditCardName;
		this.statementDate = statementDate;
		this.dueDate = dueDate;
	}

    public CreditCard() {
    	super();
    }
}
