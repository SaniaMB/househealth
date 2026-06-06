package com.project.househealth.repositories;

import com.project.househealth.entity.CareRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CareRelationshipRepository extends JpaRepository<CareRelationship, Long> {

    Optional<CareRelationship>
    findByObserver_UserIdAndTrackedUser_UserId(
            Long observerId,
            Long trackedUserId
    );

    List<CareRelationship>
    findByObserver_UserId(Long observerId);

    List<CareRelationship>
    findByTrackedUser_UserId(Long trackedUserId);

    boolean existsByObserver_UserIdAndTrackedUser_UserId(
            Long observerId,
            Long trackedUserId
    );
}
