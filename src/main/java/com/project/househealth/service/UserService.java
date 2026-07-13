package com.project.househealth.service;

import com.project.househealth.dto.request.UpdateNameRequest;
import com.project.househealth.dto.response.UserResponse;
import com.project.househealth.entity.User;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User updateName(String name);
}
