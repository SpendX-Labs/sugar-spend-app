package com.finance.sugarmarket.app.repo;

import com.finance.sugarmarket.app.model.MutualFund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MutualFundRepo extends JpaRepository<MutualFund, Long> {
    public MutualFund findMutualFundBySchemeCode(String schemeCode);

    public MutualFund findMutualFundBySchemeName(String schemeName);
}
