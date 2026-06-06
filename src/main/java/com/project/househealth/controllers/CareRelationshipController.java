package com.project.househealth.controllers;

import com.project.househealth.entity.CareRelationship;
import com.project.househealth.service.CareRelationshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/care-relationships")
public class CareRelationshipController {

    private final CareRelationshipService service;

    public CareRelationshipController(
            CareRelationshipService service
    ) {
        this.service = service;
    }

    @PostMapping("/{trackedUserId}")
    public ResponseEntity<String> observeUser(
            @PathVariable Long trackedUserId
    ) {

        service.observeUser(trackedUserId);

        return ResponseEntity.ok(
                "User added to care circle"
        );
    }

    @DeleteMapping("/{trackedUserId}")
    public ResponseEntity<String> stopObserving(
            @PathVariable Long trackedUserId
    ) {

        service.stopObserving(trackedUserId);

        return ResponseEntity.ok(
                "Observation removed"
        );
    }

    @GetMapping("/observing")
    public ResponseEntity<List<CareRelationship>>
    getUsersIObserve() {

        return ResponseEntity.ok(
                service.getUsersIObserve()
        );
    }

    @GetMapping("/observers")
    public ResponseEntity<List<CareRelationship>>
    getMyObservers() {

        return ResponseEntity.ok(
                service.getMyObservers()
        );
    }
}
