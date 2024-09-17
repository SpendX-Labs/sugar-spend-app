package com.finance.sugarmarket.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.Loan;

public interface LoanRepo extends JpaRepository<Loan, Integer>{
	@Query("SELECT l FROM Loan l WHERE l.user.username = :username")
	public List<Loan> findByUsername(String username);
}
