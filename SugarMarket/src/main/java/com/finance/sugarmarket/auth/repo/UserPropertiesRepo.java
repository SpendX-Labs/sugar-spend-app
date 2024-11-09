package com.finance.sugarmarket.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.sugarmarket.auth.model.UserProperties;

public interface UserPropertiesRepo extends JpaRepository<UserProperties, Long> {

	public UserProperties findByName(String name);

}
