package com.finance.sugarmarket.auth.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.sugarmarket.auth.dto.AuthenticationRequest;
import com.finance.sugarmarket.auth.dto.AuthenticationResponse;
import com.finance.sugarmarket.auth.dto.GenericResponse;
import com.finance.sugarmarket.auth.dto.SignUpRequestDTO;
import com.finance.sugarmarket.auth.dto.SignUpResponseDTO;
import com.finance.sugarmarket.auth.service.AuthenticationService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	private AuthenticationService authenticationService;

	private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		try {
			return ResponseEntity.ok(authenticationService.authenticate(request));
		} catch (Exception e) {
			log.error("getUserDetailsByJWT failed", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new AuthenticationResponse("Incorrect Username or password", false));
	}

	@GetMapping("/userinfo")
	public ResponseEntity<UserDetails> getUserDetailsByJWT(@RequestHeader Map<String, String> request) {
		try {
			return ResponseEntity.ok(authenticationService.getUserDetailsByToken(request.get("authorization")));
		} catch (Exception e) {
			log.error("getUserDetailsByJWT failed", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	}

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO request) {
		try {
			return ResponseEntity.ok(authenticationService.signup(request));
		} catch (Exception e) {
			log.error("getUserDetailsByJWT failed", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new SignUpResponseDTO("There are some internal error", false));
	}

	@PostMapping("/verifyotp")
	public ResponseEntity<SignUpResponseDTO> verifyotp(@RequestBody SignUpRequestDTO request) {
		try {
			return ResponseEntity.ok(authenticationService.verifyotp(request));
		} catch (Exception e) {
			log.error("getUserDetailsByJWT failed", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new SignUpResponseDTO("There are some internal error", false));
	}

	@PostMapping("/forget-password")
	public ResponseEntity<GenericResponse> forgetPassword(@RequestBody Map<String, String> request) {
		try {
			return ResponseEntity.ok(authenticationService.forgetPassword(request));
		} catch (Exception e) {
			log.error("forgetPassword failed", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new GenericResponse("Failed to reset password", false));
	}

}