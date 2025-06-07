package com.finance.sugarmarket.app.model;

import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.enums.TransactionType;
import com.finance.sugarmarket.auth.model.MFUser;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private MFUser user;

    @Enumerated(EnumType.STRING)
    private CashFlowType cashFlowType; //DEBIT or CREDIT

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;  // CREDITCARD or BANK or CASH

    @ManyToOne
    @JoinColumn(name = "bankAccountId", referencedColumnName = "bankAccountId")
    private BankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "creditCardId", referencedColumnName = "creditCardId")
    private CreditCard creditCard;

    private BigDecimal amount;

    private Date transactionDate;

    private String note;

    // Constructors

    public Transaction() {
    }

    public Transaction(Transaction transaction) {
        this.user = transaction.user;
        this.transactionType = transaction.transactionType;
        this.cashFlowType = transaction.cashFlowType;
        this.bankAccount = transaction.bankAccount;
        this.creditCard = transaction.creditCard;
        this.amount = transaction.amount;
        this.transactionDate = transaction.transactionDate;
        this.note = transaction.note;
    }

    // Getters and Setters

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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public CashFlowType getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(CashFlowType cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
