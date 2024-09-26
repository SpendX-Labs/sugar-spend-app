package com.finance.sugarmarket.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.finance.sugarmarket.app.model.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {

}
