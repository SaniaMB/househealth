package com.project.househealth.service;

import com.project.househealth.entity.User;

public interface AuthService {
    User login(String email, String password);
}
