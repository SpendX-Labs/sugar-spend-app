package com.finance.sugarmarket.app.repo;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.Income;

public interface IncomeRepo extends JpaRepository<Income, Long>, JpaSpecificationExecutor<Income> {

	@Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId AND YEAR(i.dateOfEvent) = :year AND MONTHNAME(i.dateOfEvent) = :month")
	BigDecimal getTotalMonthlyIncome(Integer year, String month, Long userId);

	@Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId AND YEAR(i.dateOfEvent) = :year")
	BigDecimal getTotalYearlyIncome(Integer year, Long userId);

}
