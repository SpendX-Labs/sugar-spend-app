package com.finance.sugarmarket.base.util;

public class EmailTemplateUtil {
    public static String createSimpleOTPHtmlTemplate(String fullName, String otp) {
        return String.format("""
                 <!DOCTYPE html>
                 <html>
                 <head>
                     <meta charset="UTF-8">
                     <meta name="viewport" content="width=device-width, initial-scale=1.0">
                     <style>
                         body { font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; }
                         .otp-container { background: #f8f9fa; padding: 30px; text-align: center; border-radius: 8px; margin: 20px 0; }
                         .otp-code { font-size: 32px; font-weight: bold; color: #007bff; letter-spacing: 5px; margin: 15px 0; }
                         .warning { background: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #ffc107; }
                     </style>
                 </head>
                 <body>
                     <h2>Hi %s,</h2>
                     <p>Your OTP for Sugar Spend verification:</p>
                    \s
                     <div class="otp-container">
                         <p>Your Verification Code</p>
                         <div class="otp-code">%s</div>
                         <p><small>Valid for 10 minutes</small></p>
                     </div>
                    \s
                     <div class="warning">
                         <strong>Security Notice:</strong> Never share this OTP with anyone.\s
                         Sugar Spend will never ask for your OTP.
                     </div>
                    \s
                     <p>If you didn't request this, please ignore this email.</p>
                    \s
                     <p>Thanks,<br>Sugar Spend Team</p>
                 </body>
                 </html>
                \s""", fullName, otp);
    }
}
