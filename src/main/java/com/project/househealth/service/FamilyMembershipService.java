package com.project.househealth.service;

import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;

public interface FamilyMembershipService {
    FamilyMembership getFamilyMembershipById(Long id);
    void leaveFamily(Long familyId);
    void addMember(Long familyId,Long targetUserId);
    void removeMember(Long familyId, Long targetUserId);
    void addOwner(Long familyId, Long targetUserId);
    void transferOwnership(Long familyId, Long newOwnerUserId);
}
