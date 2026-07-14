package com.project.househealth.service;

import com.project.househealth.dto.request.UpdateNameRequest;
import com.project.househealth.dto.response.UserResponse;
import com.project.househealth.entity.User;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id) throws AccessDeniedException;
    User getUserByEmail(String email);
    User updateName(String name);

    User findUserById(Long id);
}
