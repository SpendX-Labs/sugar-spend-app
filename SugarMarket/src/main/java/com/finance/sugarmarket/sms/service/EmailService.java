package com.finance.sugarmarket.sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements SMSService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSMS(String toEmail, String subject, String body)  throws Exception {
        log.info("Sending email to: " + toEmail);
        try {
        	SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("fromemail@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("ERROR in mail sent method: ", e);
            throw new Exception("Error while sending email");
        }
    }
}