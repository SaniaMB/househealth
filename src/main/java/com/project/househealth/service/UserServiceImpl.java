package com.project.househealth.service;

import com.project.househealth.entity.User;
import com.project.househealth.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class
UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public  UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
