package com.project.househealth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(
            String recipientEmail,
            String verificationLink
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject(
                "Verify your HouseHealth account"
        );

        message.setText(
                """
                Welcome to HouseHealth!

                Please verify your email by clicking the link below:

                %s

                This link expires in 24 hours.
                """
                        .formatted(verificationLink)
        );

        mailSender.send(message);
    }
}