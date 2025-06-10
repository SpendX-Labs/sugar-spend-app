package com.finance.sugarmarket.auth.repo;

import com.finance.sugarmarket.auth.model.MapRoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MapRoleUserRepo extends JpaRepository<MapRoleUser, Long> {
    @Query("SELECT maproleuser FROM MapRoleUser maproleuser WHERE maproleuser.user.id=:userId")
    public MapRoleUser findByUser(@Param("userId") Long userId);
}