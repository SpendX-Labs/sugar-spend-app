package com.finance.sugarmarket.sms.service;

public interface SMSService {
	
	public void sendSMS(String toEmail, String subject, String body) throws Exception;

}
