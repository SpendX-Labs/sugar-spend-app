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
			+ "WHERE b.user.id = :userId AND b.creditCard IS NOT NULL AND " + "b.budgetYear = :year "
			+ "GROUP BY b.creditCard.id")
	List<Object[]> findAllByCreditCardYear(Integer year, Long userId);

	@Query("SELECT b.loan, SUM(b.actualAmount), SUM(b.remainingAmount) " + "FROM BudgetView b "
			+ "WHERE b.user.id = :userId AND b.loan IS NOT NULL AND " + "b.budgetYear = :year " + "GROUP BY b.loan.id")
	List<Object[]> findAllByLoanYear(Integer year, Long userId);

	@Query("SELECT b FROM BudgetView b WHERE b.user.id = :userId AND b.budgetMonth=:month AND b.budgetYear = :year ORDER BY b.dueDate")
	List<BudgetView> findAllByMonth(Integer year, String month, Long userId);

	@Query("SELECT new com.finance.sugarmarket.app.model.BudgetView(b.user, b.budgetMonth, b.budgetYear, SUM(b.actualAmount), SUM(b.remainingAmount), MIN(b.updateDate), MIN(b.dueDate), "
			+ "CASE WHEN b.creditCard IS NOT NULL THEN b.creditCard ELSE b.loan END, "
			+ "CASE WHEN b.creditCard IS NOT NULL THEN NULL ELSE b.creditCard END) " + "FROM BudgetView b "
			+ "WHERE b.user.id = :userId AND b.budgetYear = :year "
			+ "GROUP BY b.user.id, b.budgetMonth, b.budgetYear, b.creditCard.id, b.loan.id "
			+ "ORDER BY MIN(b.dueDate)")
	List<BudgetView> findAllByYear(Integer year, Long userId);
}
