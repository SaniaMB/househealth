package com.project.househealth.service;

import com.project.househealth.entity.CareRelationship;

import java.util.List;

public interface CareRelationshipService {

    void observeUser(Long trackedUserId);

    void stopObserving(Long trackedUserId);

    List<CareRelationship> getUsersIObserve();

    List<CareRelationship> getMyObservers();
}