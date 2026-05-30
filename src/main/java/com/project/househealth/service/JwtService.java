package com.project.househealth.service;

import com.project.househealth.entity.User;
import com.project.househealth.enums.SystemRole;

public interface JwtService {
    String generateToken(User user);
    String extractEmail(String token);
    Long extractUserId(String token);
    SystemRole extractRole(String token);
    boolean isTokenExpired(String token);
    boolean validateToken(String token);
}
