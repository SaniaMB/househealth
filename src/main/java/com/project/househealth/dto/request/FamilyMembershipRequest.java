package com.project.househealth.dto.request;

public class FamilyMembershipRequest {
    private boolean notificationsEnabled;

    public FamilyMembershipRequest() {}

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
