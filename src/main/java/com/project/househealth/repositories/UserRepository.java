package com.project.househealth.repositories;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
