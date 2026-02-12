package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.enums.Role;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.FamilyRepository;
import com.project.househealth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FamilyServiceImpl implements FamilyService {

    private final UserService userService;
    private final FamilyRepository familyRepository;
    private final FamilyMembershipRepository familyMembershipRepository;

    public FamilyServiceImpl(UserService userService,FamilyMembershipRepository familyMembershipRepository,
                              FamilyRepository familyRepository){
        this.userService = userService;
        this.familyRepository = familyRepository;
        this.familyMembershipRepository = familyMembershipRepository;
    }

    @Override
    public Family createFamily(String familyName, Long creatorUserId) {

        User user = userService.getUserById(creatorUserId);

        Family family = new Family(familyName);
        family = familyRepository.save(family);

        FamilyMembership familyMembership = new FamilyMembership(user, family, Role.BOTH);
        familyMembership.makeOwner();

        familyMembershipRepository.save(familyMembership);

        return family;
    }

    @Override
    public Family getFamilyById(Long id) {
        return familyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family not found"));
    }

    @Override
    public void renameFamily(Long familyId, String newName, Long actingUserId) {
        Family family = getFamilyById(familyId);
        User actingUser = userService.getUserById(actingUserId);

        FamilyMembership membership =
                familyMembershipRepository.findByUserAndFamily(actingUser, family)
                        .orElseThrow(() -> new RuntimeException("Family membership not found"));

        if(!membership.isOwner()){
            throw new RuntimeException("Only owner can rename family");
        }

        family.renameFamily(newName);
    }

    @Override
    public void deleteFamily(Long familyId, Long actingUserId) {
        User actingUser = userService.getUserById(actingUserId);
        Family family = getFamilyById(familyId);

        FamilyMembership membership =
                familyMembershipRepository.findByUserAndFamily(actingUser, family)
                        .orElseThrow(() -> new RuntimeException("Family membership not found"));

        if(!membership.isOwner()){
            throw new RuntimeException("Only owner can delete family");
        }

        familyRepository.delete(family);
    }
}
