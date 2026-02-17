package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.enums.Role;
import com.project.househealth.enums.SystemRole;
import com.project.househealth.exception.FamilyNotFoundException;
import com.project.househealth.exception.UnauthorizedFamilyActionException;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.FamilyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final UserService userService;
    private final FamilyRepository familyRepository;
    private final FamilyMembershipRepository familyMembershipRepository;

    public FamilyServiceImpl(UserService userService,
                             FamilyRepository familyRepository,
                             FamilyMembershipRepository familyMembershipRepository){
        this.userService = userService;
        this.familyRepository = familyRepository;
        this.familyMembershipRepository = familyMembershipRepository;
    }

    @Transactional
    @Override
    public Family createFamily(String familyName, Long creatorUserId) {

        User creator = userService.getUserById(creatorUserId);

        Family family = new Family(familyName);
        family.setCreatedBy(creator);
        family = familyRepository.save(family);

        FamilyMembership familyMembership = new FamilyMembership(creator, family, Role.BOTH);
        familyMembership.makeOwner();

        family.addMembership(familyMembership);

        return familyRepository.save(family);
    }

    @Transactional(readOnly = true)
    @Override
    public Family getFamilyById(Long id) {

        Family family = familyRepository.findById(id)
                .orElseThrow(() -> new FamilyNotFoundException("Family not found"));


        return family;
    }

    private Family validateOwnership(Long familyId, Long actingUserId){

        Family family = getFamilyById(familyId);

        FamilyMembership membership =
                familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId)
                        .orElseThrow(() -> new UnauthorizedFamilyActionException("You are not a member of this family"));

        if (!membership.isOwner()) {
            throw new UnauthorizedFamilyActionException("Only owner can perform this action");
        }

        return family;
    }

    @Transactional
    @Override
    public void renameFamily(Long familyId, String newName, Long actingUserId) {
        Family family = validateOwnership(familyId, actingUserId);

        family.renameFamily(newName);
    }

    @Transactional
    @Override
    public void permanentlyDeleteFamily(Long familyId, Long actingUserId) {

        User user = userService.getUserById(actingUserId);

        if (user.getSystemRole() != SystemRole.ADMIN){
            throw  new UnauthorizedFamilyActionException("Only admin can permanently delete a family");
        }

        Family family = getFamilyById(familyId);

        familyRepository.delete(family);
    }
}
