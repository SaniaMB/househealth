package com.project.househealth.repositories;

import com.project.househealth.entity.FamilyInvitation;
import com.project.househealth.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamilyInvitationRepository
        extends JpaRepository<FamilyInvitation, Long> {

    List<FamilyInvitation>
    findByInvitedEmailAndStatusOrderByCreatedAtDesc(
            String invitedEmail,
            InvitationStatus status
    );

    Optional<FamilyInvitation>
    findByFamily_FamilyIdAndInvitedEmailAndStatus(
            Long familyId,
            String invitedEmail,
            InvitationStatus status
    );

}