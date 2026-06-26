package com.project.househealth.service;

import com.project.househealth.entity.User;

public interface EmailService {

    void sendVerificationEmail( String recipientEmail,
                                String verificationLink );

    void sendPasswordResetEmail(
            String email,
            String resetLink
    );
}
