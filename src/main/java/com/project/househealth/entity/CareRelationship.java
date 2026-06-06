package com.project.househealth.entity;

import com.project.househealth.entity.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "care_relationships",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"observer_user_id", "tracked_user_id"}
        )
)
public class CareRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careRelationshipId;

    @ManyToOne
    @JoinColumn(name = "observer_user_id", nullable = false)
    private User observer;

    @ManyToOne
    @JoinColumn(name = "tracked_user_id", nullable = false)
    private User trackedUser;

    @Column(nullable = false)
    private boolean notificationsEnabled = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected CareRelationship() {}

    public CareRelationship(User observer, User trackedUser) {
        if (observer == null || trackedUser == null) {
            throw new IllegalArgumentException("Users cannot be null");
        }

        if (observer.getUserId().equals(trackedUser.getUserId())) {
            throw new IllegalArgumentException("Cannot observe yourself");
        }

        this.observer = observer;
        this.trackedUser = trackedUser;
    }

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }

    public User getObserver() {
        return observer;
    }

    public User getTrackedUser() {
        return trackedUser;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void enableNotifications() {
        notificationsEnabled = true;
    }

    public void disableNotifications() {
        notificationsEnabled = false;
    }
}
