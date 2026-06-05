package com.project.househealth.controllers;

import com.project.househealth.dto.request.LoginRequest;
import com.project.househealth.dto.response.AuthResponse;
import com.project.househealth.entity.User;
import com.project.househealth.service.AuthService;
import com.project.househealth.service.JwtService;
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

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
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

}
