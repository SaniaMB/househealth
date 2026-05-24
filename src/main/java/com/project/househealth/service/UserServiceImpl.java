package com.project.househealth.service;

import com.project.househealth.entity.User;
import com.project.househealth.exception.UserNotFoundException;
import com.project.househealth.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class
UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public  UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private void validateUser(User user){
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }

        if(user.getName() == null || user.getName().isBlank()){
            throw new IllegalArgumentException("Name cannot be blank");
        }

        if(user.getEmail() == null || user.getEmail().isBlank()){
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if(user.getPasswordHash() == null || user.getPasswordHash().isBlank()){
            throw new IllegalArgumentException("\"Password hash cannot be blank\"");
        }
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
