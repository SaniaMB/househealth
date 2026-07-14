package com.project.househealth.service;

import com.project.househealth.entity.User;
import com.project.househealth.enums.SystemRole;
import com.project.househealth.exception.EmailAlreadyExistsException;
import com.project.househealth.exception.UserNotFoundException;
import com.project.househealth.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final CurrentUserService currentUserService;

    public  UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailVerificationService emailVerificationService, CurrentUserService currentUserService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
        this.currentUserService = currentUserService;
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
    public User createUser(User user){

        validateUser(user);

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        String hashedPassword =
                passwordEncoder.encode(
                        user.getPasswordHash()
                );

        user.setPasswordHash(hashedPassword);

        User savedUser = userRepository.save(user);

        emailVerificationService.sendVerificationEmail(savedUser);

        return savedUser;

    }

    @Override
    public User getUserById(Long id) throws AccessDeniedException {

        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getSystemRole().equals(SystemRole.ADMIN) &&
                !currentUser.getUserId().equals(id)) {
            throw new UserNotFoundException("User not found");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User updateName(String name) {

        User user = currentUserService.getCurrentUser();

        user.updateName(name);

        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
