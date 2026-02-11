package com.project.househealth.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<HealthLog> healthLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ReminderSettings> reminderSettings = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FamilyMembership> familyMemberships = new ArrayList<>();

    protected User(){
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    public User(String name, String passwordHash, String email){
       this.name = name;
       this.passwordHash = passwordHash;
       this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<HealthLog> getHealthLogs() {
        return Collections.unmodifiableList(healthLogs);
    }

    public List<ReminderSettings> getReminderSettings() {
        return Collections.unmodifiableList(reminderSettings);
    }

    public List<FamilyMembership> getFamilyMemberships() {
        return Collections.unmodifiableList(familyMemberships);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

    @Override
    public String toString() {
        return "UserId=" + userId +
                ", UserName='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", Email=" + email +
                ", Password=" + passwordHash +'}';
    }
}
