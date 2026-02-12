package com.project.househealth.repositories;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyMembershipRepository extends JpaRepository<FamilyMembership, Long> {
    boolean existsByUserAndFamily(User user, Family family);
    boolean existsByFamilyAndOwnerTrue(Family family);
    Optional<FamilyMembership> findByUserAndFamily(User user, Family family);
}
