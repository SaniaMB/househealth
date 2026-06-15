package com.project.househealth.service;

import com.project.househealth.entity.User;
import com.project.househealth.entity.VerificationToken;
import com.project.househealth.repositories.UserRepository;
import com.project.househealth.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService{

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailVerificationServiceImpl(VerificationTokenRepository verificationTokenRepository, UserRepository userRepository, EmailService emailService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public void sendVerificationEmail(User user){

        String token = UUID.randomUUID().toString();

        Instant expiresAt = Instant.now().plus(24, ChronoUnit.HOURS);

        VerificationToken verificationToken =
                new VerificationToken(token, user, expiresAt);

        verificationTokenRepository.save(verificationToken);

        String verificationLink =
                frontendUrl + "/verify?token=" + token;

        emailService.sendVerificationEmail(
                user.getEmail(),
                verificationLink
        );
    }

    @Override
    public User verifyToken(String token) {

        VerificationToken verificationToken =
                verificationTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid verification token"
                                ));

        if (verificationToken.isUsed()) {
            throw new IllegalStateException(
                    "Verification token has already been used"
            );
        }

        if (verificationToken.isExpired()) {
            throw new IllegalStateException(
                    "Verification token has expired"
            );
        }

        User user = verificationToken.getUser();

        if (!user.isEmailVerified()) {
            user.verifyEmail();
            userRepository.save(user);
        }

        verificationToken.markAsUsed();
        verificationTokenRepository.save(verificationToken);

        return user;
    }

}

