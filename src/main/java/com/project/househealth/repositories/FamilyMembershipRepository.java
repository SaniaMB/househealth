package com.project.househealth.repositories;

import com.project.househealth.entity.FamilyMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMembershipRepository extends JpaRepository<FamilyMembership, Long> {
}
