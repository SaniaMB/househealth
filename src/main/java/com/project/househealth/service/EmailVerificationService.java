package com.project.househealth.service;

import com.project.househealth.entity.User;

public interface EmailVerificationService {

    public void sendVerificationEmail(User user);

    public User verifyToken(String token);
}
