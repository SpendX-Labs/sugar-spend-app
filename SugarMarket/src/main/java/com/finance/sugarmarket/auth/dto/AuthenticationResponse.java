package com.finance.sugarmarket.auth.dto;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationResponse extends GenericResponse {

	private String token;
	private UserDetailsDTO userDetails;

	public AuthenticationResponse(String token, UserDetailsDTO userDetails, Boolean status) {
		super();
		this.token = token;
		this.userDetails = userDetails;
		this.setStatus(status);
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

	public UserDetailsDTO getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetailsDTO userDetails) {
		this.userDetails = userDetails;
	}
}