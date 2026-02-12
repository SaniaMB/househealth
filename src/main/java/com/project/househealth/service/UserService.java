package com.project.househealth.service;

import com.project.househealth.entity.User;

import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
}
