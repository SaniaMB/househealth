package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.enums.Role;
import com.project.househealth.exception.AlreadyMemberException;
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

    @Override
    public FamilyMembership createFamilyMembership(FamilyMembership familyMembership) {
        return familyMembershipRepository.save(familyMembership);
    }

    @Override
    public FamilyMembership getFamilyMembershipById(Long id) {
        return familyMembershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family membership not found"));
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
}
