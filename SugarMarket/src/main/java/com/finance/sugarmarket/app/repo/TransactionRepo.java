package com.finance.sugarmarket.app.repo;

import com.finance.sugarmarket.app.enums.TransactionType;
import com.finance.sugarmarket.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findAllByCreditCardId(Long creditCardId);

    List<Transaction> findAllByBankAccountId(Long id);

    @Query("SELECT t.transactionDate, t.transactionType, SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId "
            + "AND YEAR(t.transactionDate) = :year AND MONTHNAME(t.transactionDate) = :month AND t.cashFlowType = 'DEBIT'"
            + "GROUP BY t.transactionDate, t.transactionType")
    List<Object[]> getMonthlyExpenseSummaryWithCashFlow(Integer year, String month, Long userId);

    @Query("SELECT MONTHNAME(t.transactionDate), t.transactionType, SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId "
            + "AND YEAR(t.transactionDate) = :year AND t.cashFlowType = 'DEBIT' GROUP BY MONTHNAME(t.transactionDate), t.transactionType")
    List<Object[]> getYearlyExpenseSummaryWithCashFlow(Integer year, Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND YEAR(t.transactionDate) = :year" +
            " AND MONTHNAME(t.transactionDate) = :month AND t.cashFlowType = 'CREDIT'")
    BigDecimal getTotalMonthlyIncome(Integer year, String month, Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND YEAR(t.transactionDate) = :year AND t.cashFlowType = 'CREDIT'")
    BigDecimal getTotalYearlyIncome(Integer year, Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.creditCard.id = :creditCardId AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpenseOfCreditCardByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("creditCardId") Long creditCardId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND YEAR(t.transactionDate) = :year" +
            " AND MONTHNAME(t.transactionDate) = :month AND t.cashFlowType = 'DEBIT' AND t.transactionType in (:transactionTypes)")
    BigDecimal getTotalMonthlyExpenseByTransactionType(Integer year, String month, List<TransactionType> transactionTypes, Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND YEAR(t.transactionDate) = :year" +
            " AND t.cashFlowType = 'DEBIT' AND t.transactionType in (:transactionTypes)")
    BigDecimal getTotalYearlyExpenseByTransactionType(Integer year, List<TransactionType> transactionTypes, Long userId);
}
