package com.project.househealth.service;

import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.repositories.FamilyMembershipRepository;
import org.springframework.stereotype.Service;

@Service
public class FamilyMembershipServiceImpl implements FamilyMembershipService{

    private final FamilyMembershipRepository familyMembershipRepository;

    public FamilyMembershipServiceImpl(FamilyMembershipRepository familyMembershipRepository){
        this.familyMembershipRepository = familyMembershipRepository;
    }

    @Override
    public FamilyMembership createFamilyMembership(FamilyMembership familyMembership) {
        return familyMembershipRepository.save(familyMembership);
    }

    @Override
    public FamilyMembership getFamilyMembershipById(Long id) {
        return familyMembershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family membership not found"));
    }
}
