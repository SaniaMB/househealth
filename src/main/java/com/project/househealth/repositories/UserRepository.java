package com.project.househealth.repositories;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
