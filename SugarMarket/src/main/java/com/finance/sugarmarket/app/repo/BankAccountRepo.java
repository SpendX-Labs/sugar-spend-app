package com.finance.sugarmarket.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {
	
	@Query("SELECT b FROM BankAccount b WHERE b.user.id = :userId")
	public List<BankAccount> findByUserId(Long userId);
}
