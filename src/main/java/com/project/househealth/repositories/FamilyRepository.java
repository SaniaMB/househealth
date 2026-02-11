package com.project.househealth.repositories;

import com.project.househealth.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FamilyRepository extends JpaRepository<Family, Long> {
    List<Family>  findByFamilyName(String familyName);
}
