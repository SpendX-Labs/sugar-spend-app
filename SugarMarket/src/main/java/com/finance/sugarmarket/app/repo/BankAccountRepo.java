package com.finance.sugarmarket.app.repo;

import com.finance.sugarmarket.app.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {

    @Query("SELECT b FROM BankAccount b WHERE b.user.id = :userId")
    public List<BankAccount> findByUserId(Long userId);

    @Query("SELECT b FROM BankAccount b WHERE b.user.id = :userId AND b.last4Digit LIKE :last4Digit")
    public BankAccount findByUserIdAndLast4Digit(Long userId, String last4Digit);
}
