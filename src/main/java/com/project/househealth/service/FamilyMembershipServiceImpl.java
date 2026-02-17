package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.enums.Role;
import com.project.househealth.exception.AlreadyMemberException;
import com.project.househealth.exception.IllegalOperationException;
import com.project.househealth.exception.MembershipNotFoundException;
import com.project.househealth.exception.UnauthorizedFamilyActionException;
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

    public FamilyMembershipServiceImpl(UserService userService,
                                        FamilyService familyService,
                                        FamilyRepository familyRepository,
                                        FamilyMembershipRepository familyMembershipRepository){
        this.userService = userService;
        this.familyService = familyService;
        this.familyRepository = familyRepository;
        this.familyMembershipRepository = familyMembershipRepository;
    }

    private FamilyMembership createFamilyMembership(FamilyMembership familyMembership) {
        return familyMembershipRepository.save(familyMembership);
    }

    @Override
    public FamilyMembership getFamilyMembershipById(Long id) {
        return familyMembershipRepository.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException("Family membership not found"));
    }

    @Transactional
    @Override
    public void leaveFamily(Long familyId,Long actingUserId) {

        FamilyMembership membership =
                familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId)
                        .orElseThrow(() -> new UnauthorizedFamilyActionException("You are not a member of this family"));

        Family family = membership.getFamily();

        if(membership.isOwner()){
            long ownerCount = familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId);

            if(ownerCount <= 1){
                throw  new UnauthorizedFamilyActionException("Cannot leave as the last owner. Transfer ownership first");
            }
        }

        familyMembershipRepository.delete(membership);

        long familyMembershipCount = familyMembershipRepository.countByFamily_FamilyId(familyId);

        if(familyMembershipCount == 0) {
           familyRepository.delete(family);
        }

    }

    @Transactional
    @Override
    public void addMember(Long familyId,Long targetUserId,Long actingUserId) {

        FamilyMembership actingMembership =  familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId)
                .orElseThrow(() -> new UnauthorizedFamilyActionException("You are not a member of this family"));

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

        User targetUser = userService.getUserById(targetUserId);
        FamilyMembership newMembership = new FamilyMembership(targetUser, family, Role.OBSERVER);

        familyMembershipRepository.save(newMembership);
    }

    @Transactional
    @Override
    public void removeMember(Long familyId, Long targetUserId, Long actingUserId) {

        Family family = familyService.getFamilyById(familyId);

        FamilyMembership actingMembership = familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId)
                .orElseThrow(() -> new UnauthorizedFamilyActionException("You are not a member of this family"));

        if (!actingMembership.isOwner()) {
            throw new UnauthorizedFamilyActionException("Only owner can remove a member");
        }

        FamilyMembership targetMembership = familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId)
                .orElseThrow(() -> new MembershipNotFoundException("You are not a member of this family"));

        boolean isSelfRemoval = actingUserId.equals(targetUserId);

        if (targetMembership.isOwner()) {
            long ownerCount = familyMembershipRepository
                    .countByFamily_FamilyIdAndOwnerTrue(familyId);

            long totalCount = familyMembershipRepository
                    .countByFamily_FamilyId(familyId);

            // Owner trying to remove another owner → always block
            if (!isSelfRemoval) {
                throw new IllegalOperationException("Owners cannot remove other owners");
            }

            // Self-removal rules
            if (ownerCount == 1 && totalCount > 1) {
                throw new IllegalOperationException(
                        "Transfer ownership before leaving the family"
                );
            }
        }

        // Perform deletion
        familyMembershipRepository.delete(targetMembership);

        // Cleanup: delete family if no memberships remain
        long remainingCount = familyMembershipRepository
                .countByFamily_FamilyId(familyId);

        if (remainingCount == 0) {
            familyRepository.delete(family);
        }
    }

    @Transactional
    @Override
    public void addOwner(Long familyId, Long targetUserId, Long actingUserId) {

        // Fetch acting membership
        FamilyMembership actingMembership = familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId)
                .orElseThrow(() ->
                        new UnauthorizedFamilyActionException("You are not a member of this family"));

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
                                  Long newOwnerUserId,
                                  Long actingUserId) {

        FamilyMembership actingMembership = familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId)
                .orElseThrow(() ->
                        new UnauthorizedFamilyActionException("You are not a member of this family"));

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
