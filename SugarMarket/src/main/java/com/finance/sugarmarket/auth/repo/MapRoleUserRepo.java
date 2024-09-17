package com.finance.sugarmarket.auth.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finance.sugarmarket.auth.model.MapRoleUser;

public interface MapRoleUserRepo extends JpaRepository<MapRoleUser, Integer>{
	@Query("SELECT maproleuser FROM MapRoleUser maproleuser WHERE maproleuser.user.id=:userId")
	public MapRoleUser findByUser(@Param("userId")Integer userId);
}