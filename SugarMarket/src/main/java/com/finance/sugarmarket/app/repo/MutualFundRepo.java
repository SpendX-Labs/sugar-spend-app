package com.finance.sugarmarket.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.sugarmarket.app.model.MutualFund;

public interface MutualFundRepo extends JpaRepository<MutualFund, Long> {
	public MutualFund findMutualFundBySchemeCode(String schemeCode);
	public MutualFund findMutualFundBySchemeName(String schemeName);
}
