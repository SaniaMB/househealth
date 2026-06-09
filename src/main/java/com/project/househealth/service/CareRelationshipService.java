package com.project.househealth.service;

import com.project.househealth.dto.response.CareRelationshipResponse;

import java.util.List;

public interface CareRelationshipService {

    void observeUser(Long trackedUserId);

    void stopObserving(Long trackedUserId);

    List<CareRelationshipResponse> getUsersIObserve();

    List<CareRelationshipResponse> getMyObservers();
}