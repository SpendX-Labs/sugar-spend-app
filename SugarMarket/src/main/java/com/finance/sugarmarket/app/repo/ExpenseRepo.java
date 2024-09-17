package com.finance.sugarmarket.app.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.Expense;

public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

	@Query("SELECT o FROM Expense o WHERE o.creditCard.user.username = :username")
	public List<Expense> findByUsername(String username);

	@Query("SELECT o FROM Expense o WHERE o.creditCard.id = :id")
	public List<Expense> findByCreditCardId(Integer id);

	@Query("SELECT e.expenseDate, SUM(e.amount) FROM Expense e WHERE e.creditCard.user.username = :username "
			+ "AND YEAR(e.expenseDate) = :year AND MONTHNAME(e.expenseDate) = :month GROUP BY e.expenseDate")
	List<Object[]> getMonthlyExpenseSummary(Integer year, String month, String username);

	@Query("SELECT MONTHNAME(e.expenseDate), SUM(e.amount) FROM Expense e WHERE e.creditCard.user.username = :username "
			+ "AND YEAR(e.expenseDate) = :year GROUP BY MONTHNAME(e.expenseDate)")
	List<Object[]> getYearlyExpenseSummary(Integer year, String username);

	@Query("SELECT e.creditCard, SUM(e.amount) FROM Expense e WHERE e.creditCard.user.username = :username "
			+ "AND YEAR(e.expenseDate) = :year "
			+ "AND (:month IS NULL OR :month='' OR MONTHNAME(e.expenseDate) = :month) " + "GROUP BY e.creditCard.id")
	List<Object[]> getCardExpenseSummary(Integer year, String month, String username);

	@Query("SELECT SUM(e.amount) FROM Expense e WHERE e.creditCard.user.username = :username "
			+ "AND YEAR(e.expenseDate) = :year "
			+ "AND (:month IS NULL OR :month='' OR MONTHNAME(e.expenseDate) = :month)")
	BigDecimal getSumAmount(Integer year, String month, String username);

}
