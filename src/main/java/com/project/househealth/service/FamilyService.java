package com.project.househealth.service;

import com.project.househealth.dto.response.FamilyMemberResponse;
import com.project.househealth.dto.response.FamilySummaryResponse;
import com.project.househealth.dto.response.MemberTrendSummaryResponse;
import com.project.househealth.entity.Family;

import java.util.List;

public interface FamilyService {
    Family createFamily(String familyName);
    Family getFamilyById(Long id);
    void renameFamily(Long familyId, String newName);
    void permanentlyDeleteFamily(Long familyId);
    List<FamilySummaryResponse> getMyFamilies();

    List<FamilyMemberResponse> getFamilyMembers(
            Long familyId
    );

    MemberTrendSummaryResponse getMemberTrendSummary(
            Long familyId,
            Long userId
    );

}
