package com.finance.sugarmarket.auth.repo;

import com.finance.sugarmarket.auth.model.UserExtraInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExtraInfoRepo extends JpaRepository<UserExtraInfo, Long> {

}
