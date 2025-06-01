package com.finance.sugarmarket.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.CreditCard;

public interface CreditCardRepo extends JpaRepository<CreditCard, Long>, JpaSpecificationExecutor<CreditCard> {
	
	@Query("SELECT cc FROM CreditCard cc WHERE cc.user.id = :userId")
	public List<CreditCard> findByUserId(Long userId);

	@Query("SELECT cc FROM CreditCard cc WHERE cc.user.id = :userId AND cc.last4Digit LIKE :last4Digit")
	public CreditCard findByUserIdAndLast4Digit(Long userId, String last4Digit);
}
