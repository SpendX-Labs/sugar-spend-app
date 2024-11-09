package com.finance.sugarmarket.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.sugarmarket.auth.service.UserPropertiesService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.AppConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/properties/user")
public class UserPropertiesController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(UserPropertiesController.class);

	@Autowired
	private UserPropertiesService userPropertiesService;

	@GetMapping
	public ResponseEntity<Map<String, String>> getAllUserProperties() {
		try {
			return ResponseEntity.ok(userPropertiesService.getAllUserProperties(getUserId()));
		} catch (Exception e) {
			log.error("error while getting user Properties: ", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
	}

	@PostMapping
	public ResponseEntity<String> saveUserProperty(@RequestBody Map<String, String> request) {
		try {
			userPropertiesService.saveUserProperty(getUserId(), request.get(AppConstants.PROPERTY),
					request.get(AppConstants.VALUE));
		} catch (Exception e) {
			log.error("error while saving user Properties: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AppConstants.FAILED);
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}
}
