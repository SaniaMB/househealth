package com.project.househealth.service;

import com.project.househealth.entity.User;

public interface JwtService {
    String generateToken(User user);
}
