package com.project.househealth.dto.request;

import com.project.househealth.enums.Role;

public class FamilyMembershipRequest {
    private Role role;
    private boolean notificationsEnabled;

    public FamilyMembershipRequest() {}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
