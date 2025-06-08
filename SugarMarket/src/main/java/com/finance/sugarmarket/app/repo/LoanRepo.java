package com.finance.sugarmarket.app.repo;

import com.finance.sugarmarket.app.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepo extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    @Query("SELECT l FROM Loan l WHERE l.user.username = :username")
    public List<Loan> findByUsername(String username);
}
