package com.finance.sugarmarket.app.model;

import com.finance.sugarmarket.auth.model.MFUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creditCardId")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private MFUser user;
    private String bankName;
    private String creditCardName;
    private Integer statementDate;
    private Integer dueDate;
    @Size(min = 4, max = 4, message = "Last 4 digits must be exactly 4 characters")
    @Pattern(regexp = "\\d{4}", message = "Last 4 digits must be numeric")
    private String last4Digit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public CreditCard(Long id, MFUser user, String bankName, String creditCardName, Integer statementDate,
                      Integer dueDate, String last4Digit) {
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
