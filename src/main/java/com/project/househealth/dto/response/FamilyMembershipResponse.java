package com.project.househealth.dto.response;

import com.project.househealth.enums.Role;

import java.time.Instant;

public class FamilyMembershipResponse {

    private Long familyMembershipId;
    private Long userId;
    private Long familyId;
    private Role role;
    private boolean owner;
    private Instant joinedAt;
    private boolean notificationsEnabled;

    public Long getFamilyMembershipId() {
        return familyMembershipId;
    }

    public void setFamilyMembershipId(Long familyMembershipId) {
        this.familyMembershipId = familyMembershipId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
