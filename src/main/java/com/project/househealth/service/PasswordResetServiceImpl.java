package com.project.househealth.service;

import com.project.househealth.entity.PasswordResetToken;
import com.project.househealth.entity.User;
import com.project.househealth.exception.ExpiredPasswordResetTokenException;
import com.project.househealth.exception.InvalidPasswordResetTokenException;
import com.project.househealth.repositories.PasswordResetTokenRepository;
import com.project.househealth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public PasswordResetServiceImpl(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void requestPasswordReset(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return;
        }

        passwordResetTokenRepository.deleteByUser(user);

        PasswordResetToken token =
                new PasswordResetToken(
                        user,
                        Instant.now().plus(30, ChronoUnit.MINUTES)
                );

        passwordResetTokenRepository.save(token);

        String resetLink =
                frontendUrl +
                        "/reset-password?token=" +
                        token.getToken();

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                resetLink
        );
    }

    @Override
    public void resetPassword(
            String token,
            String newPassword
    ) {

        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new InvalidPasswordResetTokenException(
                                        "Invalid password reset link."
                                )
                        );

        if (passwordResetToken.isExpired()) {
            passwordResetTokenRepository.delete(passwordResetToken);

            throw new ExpiredPasswordResetTokenException(
                    "Password reset link has expired."
            );
        }

        User user = passwordResetToken.getUser();

        user.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);
    }
}