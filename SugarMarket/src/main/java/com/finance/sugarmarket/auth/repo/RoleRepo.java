package com.finance.sugarmarket.auth.repo;

import com.finance.sugarmarket.auth.model.MFRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<MFRole, Long> {

}