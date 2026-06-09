package com.project.househealth.service;

import com.project.househealth.dto.response.FamilyInvitationResponse;

import java.util.List;

public interface FamilyInvitationService {

    void sendInvitation(
            Long familyId,
            String email
    );

    List<FamilyInvitationResponse>
    getMyPendingInvitations();

    void acceptInvitation(
            Long invitationId
    );

    void declineInvitation(
            Long invitationId
    );

}