package com.finance.sugarmarket.sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.Arrays;
import java.util.List;

@Service
public class EmailService implements SMSService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${brevo.api.key:api-key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email:noreply@yourdomain.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:Your App Name}")
    private String senderName;

    private TransactionalEmailsApi getApiInstance() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setApiKey(brevoApiKey);
        return new TransactionalEmailsApi();
    }

    @Override
    public void sendSMS(String toEmail, String subject, String body) throws Exception {
        log.info("Sending email via Brevo API to: {}", toEmail);

        try {
            TransactionalEmailsApi apiInstance = getApiInstance();

            // Create sender
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(senderEmail);
            sender.setName(senderName);

            // Create recipient
            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(toEmail);
            List<SendSmtpEmailTo> toList = Arrays.asList(recipient);

            // Create email
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setSubject(subject);
            sendSmtpEmail.setTextContent(body);
            // You can also use HTML content:
            // sendSmtpEmail.setHtmlContent("<html><body>" + body + "</body></html>");

            // Send email
            CreateSmtpEmail result = apiInstance.sendTransacEmail(sendSmtpEmail);
            log.info("Email sent successfully to: {}. Message ID: {}", toEmail, result.getMessageId());

        } catch (Exception e) {
            log.error("Failed to send email via Brevo API to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new Exception("Error while sending email via Brevo: " + e.getMessage());
        }
    }

    // Enhanced method with HTML support
    public void sendHtmlEmail(String toEmail, String subject, String htmlBody, String textBody) throws Exception {
        log.info("Sending HTML email via Brevo API to: {}", toEmail);

        try {
            TransactionalEmailsApi apiInstance = getApiInstance();

            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(senderEmail);
            sender.setName(senderName);

            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(toEmail);
            List<SendSmtpEmailTo> toList = Arrays.asList(recipient);

            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setSubject(subject);
            sendSmtpEmail.setHtmlContent(htmlBody);
            sendSmtpEmail.setTextContent(textBody); // Fallback for email clients that don't support HTML

            CreateSmtpEmail result = apiInstance.sendTransacEmail(sendSmtpEmail);
            log.info("HTML email sent successfully to: {}. Message ID: {}", toEmail, result.getMessageId());

        } catch (Exception e) {
            log.error("Failed to send HTML email via Brevo API to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new Exception("Error while sending HTML email via Brevo: " + e.getMessage());
        }
    }

    // Method to send email with template
    public void sendTemplateEmail(String toEmail, Long templateId, Object templateParams) throws Exception {
        log.info("Sending template email via Brevo API to: {} using template: {}", toEmail, templateId);

        try {
            TransactionalEmailsApi apiInstance = getApiInstance();

            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(toEmail);
            List<SendSmtpEmailTo> toList = Arrays.asList(recipient);

            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setTemplateId(templateId);
            sendSmtpEmail.setParams(templateParams);

            CreateSmtpEmail result = apiInstance.sendTransacEmail(sendSmtpEmail);
            log.info("Template email sent successfully to: {}. Message ID: {}", toEmail, result.getMessageId());

        } catch (Exception e) {
            log.error("Failed to send template email via Brevo API to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new Exception("Error while sending template email via Brevo: " + e.getMessage());
        }
    }
}