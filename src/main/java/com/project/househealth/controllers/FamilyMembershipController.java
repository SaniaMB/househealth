package com.project.househealth.controllers;

import com.project.househealth.dto.response.FamilyMembershipResponse;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.service.FamilyMembershipService;
import com.project.househealth.service.FamilyMembershipServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/family-membership")
public class FamilyMembershipController {

    private final FamilyMembershipService familyMembershipService;

    public FamilyMembershipController(FamilyMembershipService familyMembershipService) {
        this.familyMembershipService = familyMembershipService;
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<FamilyMembershipResponse> getFamilyMembershipById(
            @PathVariable Long membershipId
    ) {

        FamilyMembership membership =
                familyMembershipService.getFamilyMembershipById(membershipId);

        FamilyMembershipResponse response = new FamilyMembershipResponse();

        response.setFamilyMembershipId(membership.getFamilyMembershipId());
        response.setUserId(membership.getUser().getUserId());
        response.setFamilyId(membership.getFamily().getFamilyId());
        response.setRole(membership.getRole());
        response.setOwner(membership.isOwner());
        response.setJoinedAt(membership.getJoinedAt());
        response.setNotificationsEnabled(membership.getNotificationsEnabled());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{familyId}/add-member/{targetUserId}/{actingUserId}")
    public ResponseEntity<String> addMember(
            @PathVariable Long familyId,
            @PathVariable Long targetUserId,
            @PathVariable Long actingUserId
    ) {

        familyMembershipService.addMember(
                familyId,
                targetUserId,
                actingUserId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Member added successfully");
    }

    @DeleteMapping("/{familyId}/remove-member/{targetUserId}/{actingUserId}")
    public ResponseEntity<String> removeMember(
            @PathVariable Long familyId,
            @PathVariable Long targetUserId,
            @PathVariable Long actingUserId
    ) {

        familyMembershipService.removeMember(
                familyId,
                targetUserId,
                actingUserId
        );

        return ResponseEntity.ok("Member removed successfully");
    }

    @DeleteMapping("/{familyId}/leave-family/{actingUserId}")
    public ResponseEntity<String> leaveFamily(
            @PathVariable Long familyId,
            @PathVariable Long actingUserId
    ) {

        familyMembershipService.leaveFamily(
                familyId,
                actingUserId
        );

        return ResponseEntity.ok("Successfully left family");
    }

    @PatchMapping("/{familyId}/add-owner/{targetUserId}/{actingUserId}")
    public ResponseEntity<String> addOwner(
            @PathVariable Long familyId,
            @PathVariable Long targetUserId,
            @PathVariable Long actingUserId
    ) {

        familyMembershipService.addOwner(
                familyId,
                targetUserId,
                actingUserId
        );

        return ResponseEntity.ok("Owner added successfully");
    }

    @PatchMapping("/{familyId}/transfer-ownership/{newOwnerUserId}/{actingUserId}")
    public ResponseEntity<String> transferOwnership(
            @PathVariable Long familyId,
            @PathVariable Long newOwnerUserId,
            @PathVariable Long actingUserId
    ) {

        familyMembershipService.transferOwnership(
                familyId,
                newOwnerUserId,
                actingUserId
        );

        return ResponseEntity.ok("Ownership transferred successfully");
    }
}
