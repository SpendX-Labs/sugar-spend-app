package com.finance.sugarmarket.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.repo.MFUserRepo;

@Service
public class MFUserService {
	
	@Autowired
	private MFUserRepo userRepo;
	
	public MFUser getUserByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	public List<MFUser> findAllUsers() {
		return userRepo.findAll();
	}
	
	public List<MFUser> findAllActiveUsers() {
		return userRepo.findActiveUserList();
	}
	
	public MFUser findById(Long id) {
		return userRepo.findById(id).get();
	}
}
