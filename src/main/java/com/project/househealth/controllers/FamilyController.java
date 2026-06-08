package com.project.househealth.controllers;

import com.project.househealth.dto.request.FamilyRequest;
import com.project.househealth.dto.response.FamilyMemberResponse;
import com.project.househealth.dto.response.FamilyResponse;
import com.project.househealth.dto.response.FamilySummaryResponse;
import com.project.househealth.dto.response.MemberTrendSummaryResponse;
import com.project.househealth.entity.Family;
import com.project.househealth.service.FamilyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/family")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping("/create-family")
    public ResponseEntity<FamilyResponse> createFamily(
            @Valid @RequestBody FamilyRequest request
    ){

        Family family = familyService.createFamily(request.getFamilyName());

        FamilyResponse response = new FamilyResponse();

        response.setFamilyId(family.getFamilyId());
        response.setFamilyName(family.getFamilyName());
        response.setVersion(family.getVersion());
        response.setCreatedAt(family.getCreatedAt());
        response.setCreatedByUserId(family.getCreatedBy().getUserId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<FamilyResponse> getFamilyById(
            @PathVariable Long familyId
    ) {

        Family family = familyService.getFamilyById(familyId);

        FamilyResponse response = new FamilyResponse();

        response.setFamilyId(family.getFamilyId());
        response.setFamilyName(family.getFamilyName());
        response.setVersion(family.getVersion());
        response.setCreatedAt(family.getCreatedAt());
        response.setCreatedByUserId(family.getCreatedBy().getUserId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{familyId}/rename-family")
    public ResponseEntity<String> renameFamily(
            @PathVariable Long familyId,
            @Valid @RequestBody FamilyRequest request
    ) {

        familyService.renameFamily(
                familyId,
                request.getFamilyName()
        );

        return ResponseEntity.ok("Family renamed successfully");
    }

    @DeleteMapping("/{familyId}/delete-family")
    public ResponseEntity<String> permanentlyDeleteFamily(
            @PathVariable Long familyId
    ) {

        familyService.permanentlyDeleteFamily(
                familyId
        );

        return ResponseEntity.ok("Family deleted successfully");
    }

    @GetMapping("/my-families")
    public ResponseEntity<List<FamilySummaryResponse>>
    getMyFamilies() {

        return ResponseEntity.ok(
                familyService.getMyFamilies()
        );
    }

    @GetMapping("/{familyId}/members")
    public ResponseEntity<List<FamilyMemberResponse>> getFamilyMembers(
            @PathVariable Long familyId
    ) {

        return ResponseEntity.ok(
                familyService.getFamilyMembers(
                        familyId
                )
        );
    }

    @GetMapping("/{familyId}/member/{userId}/trend-summary")
    public ResponseEntity<MemberTrendSummaryResponse> getMemberTrendSummary(
            @PathVariable Long familyId,
            @PathVariable Long userId
    ) {

        return ResponseEntity
                .ok(familyService
                        .getMemberTrendSummary(familyId, userId)
        );
    }
}
