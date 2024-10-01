package com.finance.sugarmarket.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.Expense;

public interface ExpenseRepo extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

	@Query("SELECT o FROM Expense o WHERE o.creditCard.id = :id")
	public List<Expense> findByCreditCardId(Long id);

	@Query("SELECT o FROM Expense o WHERE o.bankAccount.id = :id")
	public List<Expense> findByBankAccountId(Long id);

	@Query("SELECT e.expenseDate, e.expenseType, SUM(e.amount) FROM Expense e WHERE e.user.id = :userId "
			+ "AND YEAR(e.expenseDate) = :year AND MONTHNAME(e.expenseDate) = :month "
			+ "GROUP BY e.expenseDate, e.expenseType")
	List<Object[]> getMonthlyExpenseSummaryWithCashFlow(Integer year, String month, Long userId);

	@Query("SELECT MONTHNAME(e.expenseDate), e.expenseType, SUM(e.amount) FROM Expense e WHERE e.user.id = :userId "
			+ "AND YEAR(e.expenseDate) = :year GROUP BY MONTHNAME(e.expenseDate), e.expenseType")
	List<Object[]> getYearlyExpenseSummaryWithCashFlow(Integer year, Long userId);

}
