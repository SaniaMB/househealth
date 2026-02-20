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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FamilyMembershipServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private FamilyService familyService;

    @Mock
    private FamilyMembershipRepository familyMembershipRepository;

    @InjectMocks
    private FamilyMembershipServiceImpl familyMembershipServiceImpl;

    // Testing getFamilyMembershipById()

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

    // Testing leaveFamily()
    @Test
    void shouldThrowMembershipNotFoundException() {

        Long membershipId = 1L;

        when(familyMembershipRepository.findById(membershipId))
                .thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.getFamilyMembershipById(membershipId);
        });
    }

    @Test
    void shouldThrowWhenUserIsNotMember(){

        Long familyId = 1L;
        Long userId = 10L;

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(userId, familyId))
                .thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.leaveFamily(familyId, userId);
        });
    }

    @Test
    void shouldDeleteFamilyWhenOnlyMemberWhoseAlsoOwnerLeaves(){

        Long userId = 10L;
        Long familyId = 5L;

        User user = new User("User", "pass", "user@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership familyMembership = new FamilyMembership(user, family, Role.BOTH);
        familyMembership.makeOwner();

        when(familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(userId, familyId))
                .thenReturn(Optional.of(familyMembership));

        when(familyMembershipRepository.countByFamily_FamilyId(familyId))
                .thenReturn(1L);

        when(familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId))
                .thenReturn(1L);

        familyMembershipServiceImpl.leaveFamily(familyId, userId);

        verify(familyMembershipRepository).delete(familyMembership);
        verify(familyRepository).delete(family);
    }

    @Test
    void shouldThrowWhenLastOwnerLeavesAndOtherMembersExist(){

        Long userID = 1L;
        Long familyId = 2L;

        User user = new User("User", "pass", "user@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership familyMembership = new FamilyMembership(user, family, Role.BOTH);

        familyMembership.makeOwner();

        when(familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(userID, familyId))
                .thenReturn(Optional.of(familyMembership));

        when(familyMembershipRepository.countByFamily_FamilyId(familyId))
                .thenReturn(2L);

        when(familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId))
                .thenReturn(1L);

        assertThrows(UnauthorizedFamilyActionException.class, () ->{
            familyMembershipServiceImpl.leaveFamily(familyId, userID);
        });

        verify(familyMembershipRepository, never()).delete(any());
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldAllowOwnerToLeaveWhenMultipleOwnersExist() {

        Long userID = 1L;
        Long familyId = 2L;

        User user = new User("User", "pass", "user@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership familyMembership = new FamilyMembership(user, family, Role.BOTH);

        familyMembership.makeOwner();

        when(familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(userID, familyId))
                .thenReturn(Optional.of(familyMembership));

        when(familyMembershipRepository.countByFamily_FamilyId(familyId))
                .thenReturn(6L);

        when(familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId))
                .thenReturn(2L);

        familyMembershipServiceImpl.leaveFamily(familyId, userID);

        verify(familyMembershipRepository).delete(familyMembership);
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldAllowNonOwnerToLeave(){

        Long userID = 1L;
        Long familyId = 2L;

        User user = new User("User", "pass", "user@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership familyMembership = new FamilyMembership(user, family, Role.BOTH);

        when(familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(userID, familyId))
                .thenReturn(Optional.of(familyMembership));

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository.countByFamily_FamilyId(familyId))
                .thenReturn(6L);

        familyMembershipServiceImpl.leaveFamily(familyId, userID);

        verify(familyMembershipRepository).delete(familyMembership);
        verify(familyRepository, never()).delete(any());
    }

    // Testing addMember()

    @Test
    void shouldThrowWhenActingUserIsNotMember(){

        Long actingUserId = 3L;
        Long familyId = 2L;
        Long targetUserId = 5L;

        User actingUser = new User("actingUserId", "pass", "actingUserId@gmail.com");
        Family family = new Family("Test family");

        when(familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () ->{
            familyMembershipServiceImpl.addMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenActingUserIsNotOwner(){

        Long actingUserId = 3L;
        Long familyId = 2L;
        Long targetUserId = 5L;

        User actingUser = new User("actingUserId", "pass", "actingUserId@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership familyMembership = new FamilyMembership(actingUser, family, Role.BOTH);

        when(familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(familyMembership));

        assertThrows(UnauthorizedFamilyActionException.class, () -> {
            familyMembershipServiceImpl.addMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).save(any());

        verify(familyService, never()).getFamilyById(any());
        verify(userService, never()).getUserById(any());
    }

    @Test
    void shouldThrowWhenTargetUserAlreadyMember(){

        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUserId", "pass", "actingUserId@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership ownerMembership = new FamilyMembership(actingUser, family, Role.BOTH);

        ownerMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(ownerMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(ownerMembership));

        assertThrows(AlreadyMemberException.class, () -> {
            familyMembershipServiceImpl.addMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).save(any());
    }

    @Test
    void shouldAddMemberSuccessfully(){

        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUserId", "pass", "actingUserId@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership ownerMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        ownerMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(ownerMembership));

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.empty());

        when(userService.getUserById(targetUserId))
                .thenReturn(targetUser);

        familyMembershipServiceImpl.addMember(familyId, targetUserId, actingUserId);

        verify(familyMembershipRepository).save(any(FamilyMembership.class));
    }

    // Testing removeMember()

    @Test
    void shouldThrowWhenActingUserNotAMember(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        Family family = new Family("Test family");

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.empty());

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);


        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.removeMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).delete(any());
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenActingUserIsNotAnOwner(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUserId", "pass", "actingUserId@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership membership = new FamilyMembership(actingUser, family, Role.BOTH);

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(membership));

        assertThrows(UnauthorizedFamilyActionException.class, () -> {
            familyMembershipServiceImpl.removeMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).delete(any());
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenTargetUserNotAMember(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUserId", "pass", "actingUserId@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership membership = new FamilyMembership(actingUser, family, Role.BOTH);
        membership.makeOwner();

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(membership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.removeMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).delete(any());
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenActingUserIsTargetUser(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUserId", "pass", "actingUserId@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);

        actingMembership.makeOwner();

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        assertThrows(IllegalOperationException.class, () -> {
            familyMembershipServiceImpl.removeMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).delete(any());
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenActingUserAndTargetUserAreBothOwners(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        FamilyMembership targetMembership = new FamilyMembership(actingUser, family, Role.BOTH);

        actingMembership.makeOwner();
        targetMembership.makeOwner();

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(targetMembership));

        assertThrows(IllegalOperationException.class, () -> {
            familyMembershipServiceImpl.removeMember(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never()).delete(any());
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldRemoveTheTargetUser(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        FamilyMembership targetMembership = new FamilyMembership(targetUser, family, Role.BOTH);

        actingMembership.makeOwner();

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(targetMembership));

        when(familyMembershipRepository.countByFamily_FamilyId(familyId))
                .thenReturn(1L);

        familyMembershipServiceImpl.removeMember(familyId, targetUserId, actingUserId);

        verify(familyMembershipRepository).delete(targetMembership);
        verify(familyRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteFamilyWhenRemainingCountIsZero(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");
        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        FamilyMembership targetMembership = new FamilyMembership(targetUser, family, Role.BOTH);

        actingMembership.makeOwner();

        when(familyService.getFamilyById(familyId))
                .thenReturn(family);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(targetMembership));

        when(familyMembershipRepository.countByFamily_FamilyId(familyId))
                .thenReturn(0L);

        familyMembershipServiceImpl.removeMember(familyId, targetUserId, actingUserId);

        verify(familyMembershipRepository).delete(targetMembership);
        verify(familyRepository).delete(family);
    }

    //Testing addOwner()

    @Test
    void shouldThrowWhenActingUserNotAMemberInAddOwner(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.addOwner(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never())
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);
    }

    @Test
    void shouldThrowWhenActingUserNotAnOwnerInAddOwner(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        Family family = new Family("Test family");
        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        assertThrows(UnauthorizedFamilyActionException.class, () -> {
            familyMembershipServiceImpl.addOwner(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never())
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);
    }

    @Test
    void shouldThrowWhenTargetUserNotAMemberInAddOwner(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        Family family = new Family("Test family");
        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        actingMembership.makeOwner();


        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.addOwner(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository)
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        verifyNoMoreInteractions(familyMembershipRepository);
    }

    @Test
    void shouldThrowWhenTargetUserIsAlreadyOwner(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");

        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        FamilyMembership targetMembership = new FamilyMembership(targetUser, family, Role.BOTH);

        actingMembership.makeOwner();
        targetMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(targetMembership));

        assertThrows(IllegalOperationException.class, () -> {
            familyMembershipServiceImpl.addOwner(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository)
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        verifyNoMoreInteractions(familyMembershipRepository);
    }

    @Test
    void shouldAddAnOwner(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");

        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        FamilyMembership targetMembership = new FamilyMembership(targetUser, family, Role.BOTH);

        actingMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(targetMembership));

        familyMembershipServiceImpl.addOwner(familyId, targetUserId, actingUserId);

        assertTrue(targetMembership.isOwner());

        verify(familyMembershipRepository)
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId);

        verify(familyMembershipRepository)
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);
    }

    // Testing transferOwnership()

    @Test
    void shouldThrowWhenActingUserNotAMemberInTransferOwnership(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.transferOwnership(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never())
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        verifyNoMoreInteractions(familyMembershipRepository);
    }

    @Test
    void shouldThrowWhenActingUserNotAnOwnerInTransferOwnership(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");

        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        assertThrows(UnauthorizedFamilyActionException.class, () -> {
            familyMembershipServiceImpl.transferOwnership(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never())
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        verifyNoMoreInteractions(familyMembershipRepository);
    }

    @Test
    void shouldThrowWhenOwnerCountMoreThanOne(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");

        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        actingMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId))
                .thenReturn(2L);

        assertThrows(IllegalOperationException.class, () -> {
            familyMembershipServiceImpl.transferOwnership(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository, never())
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        verifyNoMoreInteractions(familyMembershipRepository);
    }

    @Test
    void shouldThrowWhenTargetUserNotAMemberInTransferOwnership(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");

        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        actingMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.empty());

        when(familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId))
                .thenReturn(1L);

        assertThrows(MembershipNotFoundException.class, () -> {
            familyMembershipServiceImpl.transferOwnership(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository)
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        assertTrue(actingMembership.isOwner());
    }

    @Test
    void shouldThrowWhenTargetUserNotAlreadyOwner(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");

        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        FamilyMembership targetMembership = new FamilyMembership(targetUser, family, Role.BOTH);

        actingMembership.makeOwner();
        targetMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(targetMembership));

        when(familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId))
                .thenReturn(1L);

        assertThrows(IllegalOperationException.class, () -> {
            familyMembershipServiceImpl.transferOwnership(familyId, targetUserId, actingUserId);
        });

        verify(familyMembershipRepository)
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        assertTrue(actingMembership.isOwner());
        assertTrue(targetMembership.isOwner());
    }

    @Test
    void shouldSuccessfullyTransferOwnership(){
        Long targetUserId = 1L;
        Long familyId = 2L;
        Long actingUserId = 5L;

        User actingUser = new User("actingUser", "pass", "actingUser@gmail.com");
        User targetUser = new User("targetUser", "pass", "targetUser@gmail.com");

        Family family = new Family("Test family");

        FamilyMembership actingMembership = new FamilyMembership(actingUser, family, Role.BOTH);
        FamilyMembership targetMembership = new FamilyMembership(targetUser, family, Role.BOTH);

        actingMembership.makeOwner();

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId))
                .thenReturn(Optional.of(actingMembership));

        when(familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId))
                .thenReturn(Optional.of(targetMembership));

        when(familyMembershipRepository.countByFamily_FamilyIdAndOwnerTrue(familyId))
                .thenReturn(1L);


        familyMembershipServiceImpl.transferOwnership(familyId, targetUserId, actingUserId);

        verify(familyMembershipRepository)
                .findByUser_UserIdAndFamily_FamilyId(targetUserId, familyId);

        assertFalse(actingMembership.isOwner());
        assertTrue(targetMembership.isOwner());
    }
}
