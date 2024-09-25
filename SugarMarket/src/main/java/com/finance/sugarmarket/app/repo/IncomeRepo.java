package com.finance.sugarmarket.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.finance.sugarmarket.app.model.Income;

public interface IncomeRepo extends JpaRepository<Income, Long>, JpaSpecificationExecutor<Income> {

}
