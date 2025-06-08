package com.finance.sugarmarket.app.model;

import com.finance.sugarmarket.auth.model.MFUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import java.util.Date;

@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderDetailId")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "mutualFundId", referencedColumnName = "mutualFundId")
    private MutualFund mutualFund;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private MFUser user;
    private String side; // buy or sell
    private Double amount;
    private Double nav;
    private Double units;
    private Date dateOfEvent;
    @Transient
    private String totalReturn;
    @Transient
    private Double currenValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MutualFund getMutualFund() {
        return mutualFund;
    }

    public void setMutualFund(MutualFund mutualFund) {
        this.mutualFund = mutualFund;
    }

    public MFUser getUser() {
        return user;
    }

    public void setUser(MFUser user) {
        this.user = user;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getNav() {
        return nav;
    }

    public void setNav(Double nav) {
        this.nav = nav;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(Date dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public String getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(String totalReturn) {
        this.totalReturn = totalReturn;
    }

    public Double getCurrenValue() {
        return currenValue;
    }

    public void setCurrenValue(Double currenValue) {
        this.currenValue = currenValue;
    }
}
