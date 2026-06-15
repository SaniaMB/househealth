package com.project.househealth.service;

public interface EmailService {

    void sendVerificationEmail( String recipientEmail,
                                String verificationLink );
}
