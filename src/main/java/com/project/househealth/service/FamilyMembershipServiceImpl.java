package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.exception.AlreadyMemberException;
import com.project.househealth.exception.IllegalOperationException;
import com.project.househealth.exception.MembershipNotFoundException;
import com.project.househealth.exception.UnauthorizedFamilyActionException;
import com.project.househealth.repositories.FamilyInvitationRepository;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.FamilyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamilyMembershipServiceImpl implements FamilyMembershipService{

    private final UserService userService;
    private final FamilyService familyService;
    private final FamilyRepository familyRepository;
    private final FamilyMembershipRepository familyMembershipRepository;
    private final CurrentUserService currentUserService;
    private final FamilyInvitationRepository familyInvitationRepository;

    public FamilyMembershipServiceImpl(UserService userService,
                                       FamilyService familyService,
                                       FamilyRepository familyRepository,
                                       FamilyMembershipRepository familyMembershipRepository, CurrentUserService currentUserService, FamilyInvitationRepository familyInvitationRepository){
        this.userService = userService;
        this.familyService = familyService;
        this.familyRepository = familyRepository;
        this.familyMembershipRepository = familyMembershipRepository;
        this.currentUserService = currentUserService;
        this.familyInvitationRepository = familyInvitationRepository;
    }

//    private FamilyMembership createFamilyMembership(FamilyMembership familyMembership) {
//        return familyMembershipRepository.save(familyMembership);
//    }

    @Override
    public FamilyMembership getFamilyMembershipById(Long id) {
        FamilyMembership membership =
                familyMembershipRepository.findById(id)
                        .orElseThrow(() ->
                                new MembershipNotFoundException("Family membership not found"));

        Long currentUserId = currentUserService.getCurrentUserId();

        if (!membership.getUser().getUserId().equals(currentUserId)) {
            throw new MembershipNotFoundException("Family membership not found");
        }

        return membership;
    }

    @Transactional
    @Override
    public void leaveFamily(Long familyId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        FamilyMembership membership =
                familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(currentUserId, familyId)
                        .orElseThrow(() -> new MembershipNotFoundException("You are not a member of this family"));

        Family family = membership.getFamily();

        long totalMembers =
                familyMembershipRepository.countByFamily_FamilyId(familyId);

        if(membership.isOwner()){
            long ownerCount = familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId);

            if (ownerCount == 1 && totalMembers > 1) {
                throw new UnauthorizedFamilyActionException(
                        "Transfer ownership before leaving the family"
                );
            }
        }

        familyMembershipRepository.delete(membership);

        if(totalMembers == 1) {

            familyInvitationRepository
                    .deleteByFamily_FamilyId(
                            familyId
                    );

            familyRepository.delete(
                    family
            );
        }

    }

    @Transactional
    @Override
    public void addMember(Long familyId,Long targetUserId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        FamilyMembership actingMembership =  familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(currentUserId, familyId)
                .orElseThrow(() -> new MembershipNotFoundException("You are not a member of this family"));

        if(!actingMembership.isOwner()){
            throw new UnauthorizedFamilyActionException("only owners can add members");
        }

        Family family = familyService.getFamilyById(familyId);

        boolean alreadyMember =
                familyMembershipRepository
                        .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId)
                        .isPresent();

        if (alreadyMember) {
            throw new AlreadyMemberException("User is already a member of this family");
        }

        User targetUser = userService.findUserById(targetUserId);
        FamilyMembership newMembership = new FamilyMembership(targetUser, family);

        familyMembershipRepository.save(newMembership);
    }

    @Transactional
    @Override
    public void removeMember(Long familyId, Long targetUserId) {

        Family family = familyService.getFamilyById(familyId);
        Long currentUserId = currentUserService.getCurrentUserId();

        FamilyMembership actingMembership = familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(currentUserId, familyId)
                .orElseThrow(() -> new MembershipNotFoundException("You are not a member of this family"));

        if (!actingMembership.isOwner()) {
            throw new UnauthorizedFamilyActionException("Only owner can remove a member");
        }

        FamilyMembership targetMembership = familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId)
                .orElseThrow(() -> new MembershipNotFoundException("You are not a member of this family"));

        if (currentUserId.equals(targetUserId)) {
            throw new IllegalOperationException("Use leaveFamily to leave the family");
        }

        if (targetMembership.isOwner()) {
            throw new IllegalOperationException("Owners cannot remove other owners");
        }

        // Perform deletion
        familyMembershipRepository.delete(targetMembership);

    }

    @Transactional
    @Override
    public void addOwner(Long familyId, Long targetUserId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        // Fetch acting membership
        FamilyMembership actingMembership = familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(currentUserId, familyId)
                .orElseThrow(() ->
                        new MembershipNotFoundException("You are not a member of this family"));

        if (!actingMembership.isOwner()) {
            throw new UnauthorizedFamilyActionException("Only owner can promote members to owner");
        }

        // Fetch target membership
        FamilyMembership targetMembership = familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId)
                .orElseThrow(() ->
                        new MembershipNotFoundException("Target user is not a member of this family"));

        if (targetMembership.isOwner()) {
            throw new IllegalOperationException("User is already an owner");
        }

        targetMembership.makeOwner();
    }

    @Transactional
    @Override
    public void transferOwnership(Long familyId,
                                  Long newOwnerUserId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        FamilyMembership actingMembership = familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(currentUserId, familyId)
                .orElseThrow(() ->
                        new MembershipNotFoundException("You are not a member of this family"));

        if (!actingMembership.isOwner()) {
            throw new UnauthorizedFamilyActionException("Only owner can transfer ownership");
        }

        long ownerCount = familyMembershipRepository
                .countByFamily_FamilyIdAndOwnerTrue(familyId);

        if (ownerCount != 1) {
            throw new IllegalOperationException(
                    "Ownership transfer allowed only when there is exactly one owner"
            );
        }

        FamilyMembership targetMembership = familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(newOwnerUserId, familyId)
                .orElseThrow(() ->
                        new MembershipNotFoundException("Target user is not a member of this family"));

        if (targetMembership.isOwner()) {
            throw new IllegalOperationException("User is already an owner");
        }

        targetMembership.makeOwner();

        actingMembership.removeOwner();
    }

}
