package com.project.househealth.service;

import com.project.househealth.dto.response.FamilyInvitationResponse;
import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyInvitation;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.enums.InvitationStatus;
import com.project.househealth.exception.AlreadyMemberException;
import com.project.househealth.exception.IllegalOperationException;
import com.project.househealth.exception.MembershipNotFoundException;
import com.project.househealth.exception.UnauthorizedFamilyActionException;
import com.project.househealth.repositories.FamilyInvitationRepository;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FamilyInvitationServiceImpl
        implements FamilyInvitationService {

    private final FamilyInvitationRepository familyInvitationRepository;
    private final FamilyMembershipRepository familyMembershipRepository;
    private final FamilyService familyService;
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public FamilyInvitationServiceImpl(
            FamilyInvitationRepository familyInvitationRepository,
            FamilyMembershipRepository familyMembershipRepository,
            FamilyService familyService,
            CurrentUserService currentUserService,
            NotificationService notificationService,
            UserRepository userRepository
    ) {
        this.familyInvitationRepository =
                familyInvitationRepository;
        this.familyMembershipRepository =
                familyMembershipRepository;
        this.familyService =
                familyService;
        this.currentUserService =
                currentUserService;
        this.notificationService =
                notificationService;
        this.userRepository =
                userRepository;
    }

    @Override
    public void sendInvitation(
            Long familyId,
            String email
    ) {

        Long currentUserId =
                currentUserService.getCurrentUserId();

        FamilyMembership actingMembership =
                familyMembershipRepository
                        .findByUser_UserIdAndFamily_FamilyId(
                                currentUserId,
                                familyId
                        )
                        .orElseThrow(() ->
                                new MembershipNotFoundException(
                                        "You are not a member of this family"
                                )
                        );

        Family family =
                familyService.getFamilyById(
                        familyId
                );

        boolean alreadyPending =
                familyInvitationRepository
                        .findByFamily_FamilyIdAndInvitedEmailAndStatus(
                                familyId,
                                email,
                                InvitationStatus.PENDING
                        )
                        .isPresent();

        if (alreadyPending) {
            throw new IllegalOperationException(
                    "Pending invitation already exists"
            );
        }

        User invitedUser =
                userRepository
                        .findByEmail(email)
                        .orElse(null);

        if (invitedUser != null) {

            boolean alreadyMember =
                    familyMembershipRepository
                            .findByUser_UserIdAndFamily_FamilyId(
                                    invitedUser.getUserId(),
                                    familyId
                            )
                            .isPresent();

            if (alreadyMember) {
                throw new AlreadyMemberException(
                        "User is already a member"
                );
            }
        }

        FamilyInvitation invitation =
                new FamilyInvitation(
                        family,
                        actingMembership.getUser(),
                        email
                );

        familyInvitationRepository.save(
                invitation
        );

        if (invitedUser != null) {

            notificationService.createNotification(
                    invitedUser,
                    "Family Invitation",
                    actingMembership.getUser().getName()
                            + " invited you to join "
                            + family.getFamilyName()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FamilyInvitationResponse>
    getMyPendingInvitations() {

        String email =
                currentUserService
                        .getCurrentUser()
                        .getEmail();

        return familyInvitationRepository
                .findByInvitedEmailAndStatusOrderByCreatedAtDesc(
                        email,
                        InvitationStatus.PENDING
                )
                .stream()
                .map(invitation -> {

                    FamilyInvitationResponse response =
                            new FamilyInvitationResponse();

                    response.setInvitationId(
                            invitation.getInvitationId()
                    );

                    response.setFamilyId(
                            invitation.getFamily()
                                    .getFamilyId()
                    );

                    response.setFamilyName(
                            invitation.getFamily()
                                    .getFamilyName()
                    );

                    response.setInvitedByName(
                            invitation.getInvitedBy()
                                    .getName()
                    );

                    response.setStatus(
                            invitation.getStatus()
                    );

                    response.setCreatedAt(
                            invitation.getCreatedAt()
                    );

                    return response;

                })
                .toList();
    }

    @Override
    public void acceptInvitation(
            Long invitationId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        FamilyInvitation invitation =
                familyInvitationRepository
                        .findByInvitationIdAndInvitedEmail(
                                invitationId,
                                currentUser.getEmail()
                        )
                        .orElseThrow(() ->
                                new IllegalOperationException(
                                        "Invitation not found"
                                )
                        );

        if (invitation.getStatus()
                != InvitationStatus.PENDING) {

            throw new IllegalOperationException(
                    "Invitation already processed"
            );
        }

        boolean alreadyMember =
                familyMembershipRepository
                        .findByUser_UserIdAndFamily_FamilyId(
                                currentUser.getUserId(),
                                invitation.getFamily().getFamilyId()
                        )
                        .isPresent();

        if (alreadyMember) {
            throw new AlreadyMemberException(
                    "Already a family member"
            );
        }

        FamilyMembership membership =
                new FamilyMembership(
                        currentUser,
                        invitation.getFamily()
                );

        familyMembershipRepository.save(
                membership
        );

        invitation.accept();

        notificationService.createNotification(
                invitation.getInvitedBy(),
                "Invitation Accepted",
                currentUser.getName()
                        + " joined "
                        + invitation.getFamily().getFamilyName()
        );
    }

    @Override
    public void declineInvitation(
            Long invitationId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        FamilyInvitation invitation =
                familyInvitationRepository
                        .findByInvitationIdAndInvitedEmail(
                                invitationId,
                                currentUser.getEmail()
                        )
                        .orElseThrow(() ->
                                new IllegalOperationException(
                                        "Invitation not found"
                                )
                        );

        if (invitation.getStatus()
                != InvitationStatus.PENDING) {

            throw new IllegalOperationException(
                    "Invitation already processed"
            );
        }

        invitation.decline();

        notificationService.createNotification(
                invitation.getInvitedBy(),
                "Invitation Declined",
                currentUser.getName()
                        + " declined invitation to "
                        + invitation.getFamily().getFamilyName()
        );
    }
}