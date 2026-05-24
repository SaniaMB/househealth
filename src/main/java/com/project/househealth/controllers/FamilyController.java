package com.project.househealth.controllers;

import com.project.househealth.dto.request.FamilyRequest;
import com.project.househealth.dto.response.FamilyResponse;
import com.project.househealth.entity.Family;
import com.project.househealth.service.FamilyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/family")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping("/create-family/{creatorUserId}")
    public ResponseEntity<FamilyResponse> createFamily(
            @RequestBody FamilyRequest request,
            @PathVariable Long creatorUserId
    ){

        Family family = familyService.createFamily(request.getFamilyName(), creatorUserId);

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

    @PatchMapping("/{familyId}/rename-family/{actingUserId}")
    public ResponseEntity<String> renameFamily(
            @PathVariable Long familyId,
            @PathVariable Long actingUserId,
            @RequestBody FamilyRequest request
    ) {

        familyService.renameFamily(
                familyId,
                request.getFamilyName(),
                actingUserId
        );

        return ResponseEntity.ok("Family renamed successfully");
    }

    @DeleteMapping("/{familyId}/delete-family/{actingUserId}")
    public ResponseEntity<String> permanentlyDeleteFamily(
            @PathVariable Long familyId,
            @PathVariable Long actingUserId
    ) {

        familyService.permanentlyDeleteFamily(
                familyId,
                actingUserId
        );

        return ResponseEntity.ok("Family deleted successfully");
    }
}
