package com.project.househealth.service;

import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;

public interface FamilyMembershipService {
    FamilyMembership createFamilyMembership(FamilyMembership familyMembership);
    FamilyMembership getFamilyMembershipById(Long id);
    void leaveFamily(Long familyId, Long actingUserId);
    void addMember(Long familyId,Long targetUserId,Long actingUserId);
}
