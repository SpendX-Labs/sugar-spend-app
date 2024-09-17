package com.finance.sugarmarket.app.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.BudgetView;

import jakarta.transaction.Transactional;

public interface BudgetViewRepo extends JpaRepository<BudgetView, Integer> {

	@Query("SELECT o FROM BudgetView o WHERE o.creditCard.user.username = :userName AND o.budgetYear = :budgetYear AND o.budgetMonth = :budgetMonth")
	public List<BudgetView> findAllByMonthAndYear(Integer budgetYear, String budgetMonth, String userName);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "call update_budget_view(:date, :id);", nativeQuery = true)
	public void updateBudgetView(Date date, Integer id);

	@Query("SELECT b.creditCard, SUM(b.actualAmount), SUM(b.remainingAmount) " + "FROM BudgetView b "
			+ "WHERE b.creditCard.user.username = :userName AND "
			+ "b.budgetYear = :year " + "GROUP BY b.creditCard.id")
	List<Object[]> findAllByYear(Integer year, String userName);
	
}
