package com.project.househealth.service;

import com.project.househealth.entity.Family;

public interface FamilyService {
    Family createFamily(String familyName, Long creatorUserId);
    Family getFamilyById(Long id);
    void renameFamily(Long familyId, String newName, Long actingUserId);
    void deleteFamily(Long familyId, Long actingUserId);
}
