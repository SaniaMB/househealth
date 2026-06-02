package com.project.househealth.service;

import com.project.househealth.entity.Family;

public interface FamilyService {
    Family createFamily(String familyName);
    Family getFamilyById(Long id);
    void renameFamily(Long familyId, String newName);
    void permanentlyDeleteFamily(Long familyId);
}
