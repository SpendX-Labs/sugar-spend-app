package com.finance.sugarmarket.app.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mutual_fund")
public class MutualFund {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_mutual_fund_id")
	private Integer id;
	@Column(name = "amc_name")
    private String amcName;
	@Column(name = "scheme_name")
	private String schemeName;
	@Column(name = "`option`")
    private String option;
	@Column(name = "plan_type")
    private String planType;
	@Column(name = "scheme_code")
	private String schemeCode;
	@Column(name = "current_nav")
	private Double currentNav;
	@Column(name = "day_1_nav")
	private Double day1Nav;
	@Column(name = "week_1_nav")
	private Double week1Nav;
	@Column(name = "month_1_nav")
	private Double month1Nav;
	@Column(name = "month_6_nav")
	private Double month6Nav;
	@Column(name = "year_1_nav")
	private Double year1Nav;
	@Column(name = "year_3_nav")
	private Double year3Nav;
	@Column(name = "year_5_nav")
	private Double year5Nav;
	@Column(name = "first_nav")
	private Double firstNav;
	@Column(name = "last_nav_date")
	private Date lastNavDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getAmcName() {
		return amcName;
	}

	public void setAmcName(String amcName) {
		this.amcName = amcName;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public Double getCurrentNav() {
		return currentNav;
	}

	public void setCurrentNav(Double currentNav) {
		this.currentNav = currentNav;
	}

	public Double getDay1Nav() {
		return day1Nav;
	}

	public void setDay1Nav(Double day1Nav) {
		this.day1Nav = day1Nav;
	}
	
	public Double getWeek1Nav() {
		return week1Nav;
	}

	public void setWeek1Nav(Double week1Nav) {
		this.week1Nav = week1Nav;
	}

	public Double getMonth1Nav() {
		return month1Nav;
	}

	public void setMonth1Nav(Double month1Nav) {
		this.month1Nav = month1Nav;
	}

	public Double getMonth6Nav() {
		return month6Nav;
	}

	public void setMonth6Nav(Double month6Nav) {
		this.month6Nav = month6Nav;
	}

	public Double getYear1Nav() {
		return year1Nav;
	}

	public void setYear1Nav(Double year1Nav) {
		this.year1Nav = year1Nav;
	}

	public Double getYear3Nav() {
		return year3Nav;
	}

	public void setYear3Nav(Double year3Nav) {
		this.year3Nav = year3Nav;
	}

	public Double getYear5Nav() {
		return year5Nav;
	}

	public void setYear5Nav(Double year5Nav) {
		this.year5Nav = year5Nav;
	}

	public Double getFirstNav() {
		return firstNav;
	}

	public void setFirstNav(Double firstNav) {
		this.firstNav = firstNav;
	}

	public Date getLastNavDate() {
		return lastNavDate;
	}

	public void setLastNavDate(Date lastNavDate) {
		this.lastNavDate = lastNavDate;
	}

	public MutualFund() {
		super();
	}

	public MutualFund(String amcName, String schemeName, String schemeCode, String option, String planType) {
		super();
        this.amcName = amcName;
        this.schemeName = schemeName;
        this.schemeCode = schemeCode;
        this.option = option;
        this.planType = planType;
    }

}
