package com.finance.sugarmarket.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.finance.sugarmarket.auth.model.MFRole;
import com.finance.sugarmarket.auth.repo.MapRoleUserRepo;
import com.finance.sugarmarket.auth.service.JwtService;

@RestController
public class BaseController {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private MapRoleUserRepo mapRoleUserRepo;
	
	private String getToken() {
		String token = null;
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null) {
			token = requestAttributes.getRequest().getHeader("Authorization");
		}
		return token;
	}

	public String getUserName() {
		return jwtService.extractUsername(getToken());
	}
	
	public Integer getUserId() {
		return Integer.parseInt(jwtService.extractUserId(getToken()));
	}
	
	public void checkAdminRole() throws Exception {
		if(!mapRoleUserRepo.findByUser(getUserId()).getRole().getRoleName().equals(MFRole.Role.admin.name())) {
			throw new Exception("The loggedin user is not admin.");
		}
	}
	
	public void checkCustomerRole() throws Exception {
		if(!mapRoleUserRepo.findByUser(getUserId()).getRole().getRoleName().equals(MFRole.Role.customer.name())) {
			throw new Exception("The loggedin user is not customer.");
		}
	}

}
