package com.project.househealth.service;

import com.project.househealth.entity.FamilyMembership;

public interface FamilyMembershipService {
    FamilyMembership createFamilyMembership(FamilyMembership familyMembership);
    FamilyMembership getFamilyMembershipById(Long id);
}
