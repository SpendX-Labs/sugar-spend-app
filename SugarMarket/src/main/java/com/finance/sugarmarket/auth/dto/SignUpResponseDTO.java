package com.finance.sugarmarket.auth.dto;

public class SignUpResponseDTO extends GenericResponse {

    private String username;
    private String emailId;

    public SignUpResponseDTO(String username, String emailId, String message, Boolean status) {
    	super(message, status);
        this.username = username;
        this.emailId = emailId;
    }
    
    public SignUpResponseDTO(String message, Boolean status) {
    	super(message, status);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}