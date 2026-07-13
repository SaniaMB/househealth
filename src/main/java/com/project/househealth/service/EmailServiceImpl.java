package com.project.househealth.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(
            String recipientEmail,
            String subject,
            String body
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(new InternetAddress(
                    senderEmail,
                    "HouseHealth"
            ));

            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    @Override
    public void sendVerificationEmail(
            String recipientEmail,
            String verificationLink
    ) {
        sendEmail(
                recipientEmail,
                "Verify your HouseHealth account",
                """
                Welcome to HouseHealth!
    
                Please verify your email by clicking the link below:
    
                %s
    
                This link expires in 24 hours.
                """.formatted(verificationLink)
        );
    }

    @Override
    public void sendPasswordResetEmail(
            String recipientEmail,
            String resetLink
    ) {
        sendEmail(
                recipientEmail,
                "Reset your HouseHealth password",
                """
                We received a request to reset your HouseHealth password.
    
                Click the link below to create a new password:
    
                %s
    
                This link expires in 30 minutes.
    
                If you didn't request this password reset, you can safely ignore this email.
                """.formatted(resetLink)
        );
    }

}
