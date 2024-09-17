package com.finance.sugarmarket.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.sugarmarket.auth.config.UserPrincipal;
import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.service.AuthenticationService;
import com.finance.sugarmarket.base.controller.BaseController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/accounts")
public class CustomerController extends BaseController {
	@Autowired
	private AuthenticationService authenticationService;
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	@PostMapping("/authenticate")
	public ResponseEntity<UserDetails> updateUserInfo(@RequestBody UserDetailsDTO request) {
		try {
			return ResponseEntity.ok(authenticationService.saveUserDetails(request, getUserName()));
		}
		catch (Exception e) {
			log.error("updateUserInfo failed", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UserPrincipal("Incorrect Username or password."));
	}
}
