package com.finance.sugarmarket.app.repo;

import com.finance.sugarmarket.app.model.BudgetView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetViewRepo extends JpaRepository<BudgetView, Long> {

    @Query("SELECT b FROM BudgetView b WHERE b.creditCard.id = :creditCardId AND b.budgetMonth = :month AND b.budgetYear = :year")
    BudgetView findByCreditCardIdAndMonthAndYear(Long creditCardId, String month, Integer year);

    @Query("SELECT b FROM BudgetView b WHERE b.user.id = :userId AND b.budgetMonth=:month AND b.budgetYear = :year ORDER BY b.dueDate")
    List<BudgetView> findAllByMonth(Integer year, String month, Long userId);

    @Query("SELECT b FROM BudgetView b WHERE b.user.id = :userId AND b.budgetYear = :year ORDER BY b.dueDate")
    List<BudgetView> findAllByYear(Integer year, Long userId);
}
