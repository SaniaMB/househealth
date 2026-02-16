package com.project.househealth.service;

import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;

public interface FamilyMembershipService {
    FamilyMembership getFamilyMembershipById(Long id);
    void leaveFamily(Long familyId, Long actingUserId);
    void addMember(Long familyId,Long targetUserId,Long actingUserId);
    void removeMember(Long familyId, Long targetUserId, Long actingUserId);
    void addOwner(Long familyId, Long targetUserId, Long actingUserId);
    void transferOwnership(Long familyId, Long newOwnerUserId, Long actingUserId);
}
