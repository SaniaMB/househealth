package com.project.househealth.controllers;

import com.project.househealth.dto.request.ForgotPasswordRequest;
import com.project.househealth.dto.request.LoginRequest;
import com.project.househealth.dto.request.ResetPasswordRequest;
import com.project.househealth.dto.response.AuthResponse;
import com.project.househealth.dto.request.VerifyEmailRequest;
import com.project.househealth.entity.User;
import com.project.househealth.service.AuthService;
import com.project.househealth.service.EmailVerificationService;
import com.project.househealth.service.JwtService;
import com.project.househealth.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService ;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, JwtService jwtService, EmailVerificationService emailVerificationService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.emailVerificationService = emailVerificationService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(
            @Valid @RequestBody LoginRequest request
    ){

        User user = authService.login(request.getEmail(), request.getPassword());

        String token = jwtService.generateToken(user);

        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login successful");
        authResponse.setToken(token);

        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyEmail(
            @RequestBody VerifyEmailRequest request
    ) {

        User user =
                emailVerificationService
                        .verifyToken(request.getToken());

        String token =
                jwtService.generateToken(user);

        AuthResponse response =
                new AuthResponse();

        response.setMessage(
                "Email verified successfully"
        );

        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        passwordResetService.requestPasswordReset(
                request.getEmail()
        );

        AuthResponse response = new AuthResponse();

        response.setMessage(
                "If an account with that email exists, a password reset link has been sent."
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        passwordResetService.resetPassword(
                request.getToken(),
                request.getPassword()
        );

        AuthResponse response = new AuthResponse();

        response.setMessage(
                "Password reset successful."
        );

        return ResponseEntity.ok(response);
    }

}
