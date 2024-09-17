package com.finance.sugarmarket.auth.dto;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationResponse extends GenericResponse {

	private String token;
	private UserDetails userDetails;	

	public AuthenticationResponse(String token, UserDetails userDetails) {
		super();
		this.token = token;
		this.userDetails = userDetails;
	}
	
	public AuthenticationResponse(String message, Boolean status) {
		super(message, status);
	}
	
	public AuthenticationResponse() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
}