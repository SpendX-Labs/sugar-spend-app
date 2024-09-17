package com.finance.sugarmarket.app.model;

import java.util.Date;

import com.finance.sugarmarket.auth.model.MFUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_order_detail_id")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "fk_mutual_fund_id", referencedColumnName = "pk_mutual_fund_id")
	private MutualFund mutualFund;
	@ManyToOne
	@JoinColumn(name = "fk_user_id", referencedColumnName = "pk_mf_user_id")
	private MFUser user;
	@Column(name = "side") // buy or sell
	private String side;
	@Column(name = "amount")
	private Double amount;
	@Column(name = "nav")
	private Double nav;
	@Column(name = "units")
	private Double units;
	@Column(name = "date_of_event")
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
