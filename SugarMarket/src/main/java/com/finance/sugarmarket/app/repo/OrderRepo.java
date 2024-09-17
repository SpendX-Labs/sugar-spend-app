package com.finance.sugarmarket.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finance.sugarmarket.app.model.OrderDetail;

public interface OrderRepo extends JpaRepository<OrderDetail, Integer> {

	@Query("SELECT o FROM OrderDetail o WHERE o.user.username = :username ORDER BY o.dateOfEvent ASC")
	public List<OrderDetail> findByUserUsernameOrderByDateOfEventAsc(String username);
}
