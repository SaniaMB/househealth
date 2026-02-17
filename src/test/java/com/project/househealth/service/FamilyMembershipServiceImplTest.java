package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.enums.Role;
import com.project.househealth.exception.MembershipNotFoundException;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.FamilyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FamilyMembershipServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private FamilyMembershipRepository familyMembershipRepository;

    @InjectMocks
    private FamilyServiceImpl familyService;

    @InjectMocks
    private FamilyMembershipServiceImpl familyMembershipServiceImpl;

    @Test
    void shouldReturnFamilyMembershipWhenExists(){

        Long membershipId = 1L;

        User user = new User("User", "pass", "user@gmail.com");
        Family family = new Family("Test Family");

        FamilyMembership membership =
                new FamilyMembership(user, family, Role.BOTH);

        when(familyMembershipRepository.findById(membershipId))
                .thenReturn(Optional.of(membership));

        FamilyMembership result =
                familyMembershipServiceImpl.getFamilyMembershipById(membershipId);

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void shouldThrowMembershipNotFoundException() {

        Long membershipId = 1L;

        when(familyMembershipRepository.findById(membershipId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            familyMembershipServiceImpl.getFamilyMembershipById(membershipId);
        });
    }



}
