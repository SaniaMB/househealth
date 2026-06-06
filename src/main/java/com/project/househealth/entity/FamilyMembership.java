package com.project.househealth.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table( name = "family_memberships",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "family_id"}
        ))
public class FamilyMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyMembershipId;

    @Column(nullable = false)
    private boolean owner = false;

    public Instant getJoinedAt() {
        return joinedAt;
    }

    @Column(nullable = false)
    private Instant joinedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @Column(nullable = false)
    private boolean notificationsEnabled = true;

    public FamilyMembership(){}

    public FamilyMembership(User user, Family family) {
        if (user == null || family == null) {
            throw new IllegalArgumentException("User, Family and Role must not be null");
        }
        this.user = user;
        this.family = family;
    }

    public Long getFamilyMembershipId() {
        return familyMembershipId;
    }


    public boolean isOwner() {
        return owner;
    }

    @PrePersist
    private void onJoin() {
        this.joinedAt = Instant.now();
    }
    public User getUser() {
        return user;
    }

    public Family getFamily() {
        return family;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void enableNotifications() {
        this.notificationsEnabled = true;
    }

    public void disableNotifications() {
        this.notificationsEnabled = false;
    }

    public void makeOwner() {
        this.owner = true;
    }

    public void removeOwner() {
        this.owner = false;
    }


}
