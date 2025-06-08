package com.finance.sugarmarket.auth.repo;

import com.finance.sugarmarket.auth.model.UserProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPropertiesRepo extends JpaRepository<UserProperties, Long> {

    public UserProperties findByName(String name);

}
