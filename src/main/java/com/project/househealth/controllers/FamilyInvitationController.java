package com.project.househealth.controllers;

import com.project.househealth.dto.request.SendFamilyInvitationRequest;
import com.project.househealth.dto.response.FamilyInvitationResponse;
import com.project.househealth.service.FamilyInvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/family-invitations")
public class FamilyInvitationController {

    private final FamilyInvitationService familyInvitationService;

    public FamilyInvitationController(
            FamilyInvitationService familyInvitationService
    ) {
        this.familyInvitationService =
                familyInvitationService;
    }

    @PostMapping("/{familyId}")
    public ResponseEntity<String> sendInvitation(
            @PathVariable Long familyId,
            @RequestBody SendFamilyInvitationRequest request
    ) {

        familyInvitationService.sendInvitation(
                familyId,
                request.getEmail()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Invitation sent successfully");
    }

    @GetMapping("/my-pending")
    public ResponseEntity<List<FamilyInvitationResponse>>
    getMyPendingInvitations() {

        return ResponseEntity.ok(
                familyInvitationService
                        .getMyPendingInvitations()
        );
    }

    @PatchMapping("/{invitationId}/accept")
    public ResponseEntity<String> acceptInvitation(
            @PathVariable Long invitationId
    ) {

        familyInvitationService.acceptInvitation(
                invitationId
        );

        return ResponseEntity.ok(
                "Invitation accepted"
        );
    }

    @PatchMapping("/{invitationId}/decline")
    public ResponseEntity<String> declineInvitation(
            @PathVariable Long invitationId
    ) {

        familyInvitationService.declineInvitation(
                invitationId
        );

        return ResponseEntity.ok(
                "Invitation declined"
        );
    }
}