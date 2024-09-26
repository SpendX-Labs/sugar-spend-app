package com.finance.sugarmarket.app.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.BudgetView;

import jakarta.transaction.Transactional;

public interface BudgetViewRepo extends JpaRepository<BudgetView, Long> {

	@Query("SELECT o FROM BudgetView o WHERE o.creditCard.user.id = :userId AND o.budgetYear = :budgetYear AND o.budgetMonth = :budgetMonth")
	public List<BudgetView> findAllByMonthAndYear(Integer budgetYear, String budgetMonth, Long userId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "call update_budget_view(:date, :id);", nativeQuery = true)
	public void updateBudgetView(Date date, Long id);

	@Query("SELECT b.creditCard, SUM(b.actualAmount), SUM(b.remainingAmount) " + "FROM BudgetView b "
			+ "WHERE b.user.id = :userId AND b.creditCard IS NOT NULL AND "
			+ "b.budgetYear = :year " + "GROUP BY b.creditCard.id")
	List<Object[]> findAllByCreditCardYear(Integer year, Long userId);
	
	
	@Query("SELECT b.loan, SUM(b.actualAmount), SUM(b.remainingAmount) " + "FROM BudgetView b "
			+ "WHERE b.user.id = :userId AND b.creditCard IS NOT NULL AND "
			+ "b.budgetYear = :year " + "GROUP BY b.loan.id")
	List<Object[]> findAllByLoanYear(Integer year, Long userId);
	
	
//	@Query("SELECT b from ")
//	List<BudgetView> findAllByMonth(Integer year, String month, Long userId);
//	
	
}
