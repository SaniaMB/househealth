
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FamilyServiceImplTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private FamilyMembershipRepository familyMembershipRepository;

    @InjectMocks
    private FamilyServiceImpl familyService;

    @Test
    void shouldReturnFamilyWhenFamilyExists(){

        Long familyId = 1L;
        Family family  = new Family("Test Family");

        when(familyRepository.findById(familyId)).thenReturn(Optional.of(family));

        Family result = familyService.getFamilyById(familyId);

        assertNotNull(result);
        assertEquals("Test Family", result.getFamilyName());
    }

    @Test
    void shouldThrowFamilyNotFoundException(){

        Long familyId = 1L;

        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        assertThrows(FamilyNotFoundException.class, () -> {
            familyService.getFamilyById(familyId);
        });

    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin(){

        Long familyId = 1L;

        User user = new User("UserTest","somePassword","email@gmail.com");

        user.setSystemRole(SystemRole.USER);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        assertThrows(UnauthorizedFamilyActionException.class, () -> {
            familyService.permanentlyDeleteFamily(familyId);
        });
    }

    @Test
    void shouldDeleteFamilyWhenUserIsAdmin(){

        Long familyId = 1L;

        User admin = new User("AdminUser", "pass", "admin@gmail.com");
        admin.setSystemRole(SystemRole.ADMIN);

        Family family  = new Family("Test Family");

        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(family));

        familyService.permanentlyDeleteFamily(familyId);

        verify(familyRepository).delete(family);

    }

    @Test
    void shouldThrowFamilyNotFoundWhenAdminDeletesNonExistingFamily() {

        Long familyId = 1L;

        User admin = new User("AdminUser", "pass", "admin@gmail.com");
        admin.setSystemRole(SystemRole.ADMIN);

        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        assertThrows(FamilyNotFoundException.class, () -> {
            familyService.permanentlyDeleteFamily(familyId);
        });
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAMember(){

        Long familyId = 1L;
        Long userId = 10L;

        User user = new User("User", "pass", "user@gmail.com");
        Family family = new Family("Test Family");

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(userId, familyId))
                .thenReturn(Optional.empty());

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        when(familyRepository.findById(familyId))
                .thenReturn(Optional.of(family));

        assertThrows(UnauthorizedFamilyActionException.class, () ->
        {
            familyService.renameFamily(familyId, "newName");
        });
    }

    @Test
    void shouldThrowExceptionWhenUserIsMemberButNotOwner(){

        Long familyId = 1L;
        Long userId = 10L;

        User user = new User("User", "pass", "user@gmail.com");
        Family family = new Family("Test Family");

        FamilyMembership familyMembership = new FamilyMembership(user, family, Role.BOTH);

        when(familyRepository.findById(familyId))
                .thenReturn(Optional.of(family));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(userId, familyId))
                .thenReturn(Optional.of(familyMembership));

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        assertThrows(UnauthorizedFamilyActionException.class, () ->
        {
            familyService.renameFamily(familyId, "newName");
        });
    }

    @Test
    void shouldRenameFamilyWhenUserIsMemberAndOwner(){
        Long familyId = 1L;
        Long userId = 10L;

        User user = new User("Owner", "pass", "owner@gmail.com");
        Family family = new Family("Test Family");

        FamilyMembership familyMembership = new FamilyMembership(user, family, Role.BOTH);

        when(familyRepository.findById(familyId))
                .thenReturn(Optional.of(family));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(userId, familyId))
                .thenReturn(Optional.of(familyMembership));

        when(currentUserService.getCurrentUserId())
                .thenReturn(userId);

        familyMembership.makeOwner();

        familyService.renameFamily(familyId, "newName");

        assertEquals("newName", family.getFamilyName());
    }

    @Test
    void shouldCreateFamilyWithOwnerMembership() {

        String familyName = "My Family";

        User user = new User("TestUser", "pass", "test@gmail.com");

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(familyRepository.save(any(Family.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Family result = familyService.createFamily(familyName);

        assertNotNull(result);
        assertEquals(familyName, result.getFamilyName());
        assertEquals(user, result.getCreatedBy());

        assertEquals(1, result.getFamilyMemberships().size());

        FamilyMembership membership = result.getFamilyMemberships().iterator().next();
        assertTrue(membership.isOwner());
    }
}
