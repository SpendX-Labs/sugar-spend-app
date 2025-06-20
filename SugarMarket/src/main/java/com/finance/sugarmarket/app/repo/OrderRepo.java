package com.finance.sugarmarket.app.repo;

import com.finance.sugarmarket.app.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<OrderDetail, Long> {

    @Query("SELECT o FROM OrderDetail o WHERE o.user.username = :username ORDER BY o.dateOfEvent ASC")
    public List<OrderDetail> findByUserUsernameOrderByDateOfEventAsc(String username);
}
