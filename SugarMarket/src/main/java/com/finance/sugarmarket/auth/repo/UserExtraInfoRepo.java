package com.finance.sugarmarket.auth.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.sugarmarket.auth.model.UserExtraInfo;

public interface UserExtraInfoRepo extends JpaRepository<UserExtraInfo, Long> {

}
