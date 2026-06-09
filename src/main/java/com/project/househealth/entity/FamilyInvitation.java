package com.project.househealth.entity;

import com.project.househealth.enums.InvitationStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "family_invitations")
public class FamilyInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_user_id", nullable = false)
    private User invitedBy;

    @Column(nullable = false)
    private String invitedEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    protected FamilyInvitation() {
    }

    public FamilyInvitation(
            Family family,
            User invitedBy,
            String invitedEmail
    ) {
        this.family = family;
        this.invitedBy = invitedBy;
        this.invitedEmail = invitedEmail;
        this.status = InvitationStatus.PENDING;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
    }

    public void decline() {
        this.status = InvitationStatus.DECLINED;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public Family getFamily() {
        return family;
    }

    public User getInvitedBy() {
        return invitedBy;
    }

    public String getInvitedEmail() {
        return invitedEmail;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}