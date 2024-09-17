package com.finance.sugarmarket.auth.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.sugarmarket.auth.model.MFRole;

public interface RoleRepo extends JpaRepository<MFRole, Integer>{

}