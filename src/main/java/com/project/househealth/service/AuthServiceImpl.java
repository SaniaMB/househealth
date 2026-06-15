package com.project.househealth.service;

import com.project.househealth.entity.User;
import com.project.househealth.exception.EmailNotVerifiedException;
import com.project.househealth.exception.InvalidCredentialsException;
import com.project.househealth.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User user = optionalUser.get();

        boolean passwordMatches = passwordEncoder.matches(
                password,
                user.getPasswordHash());

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException(
                    "Please verify your email before logging in."
            );
        }

        return user;
    }
}
