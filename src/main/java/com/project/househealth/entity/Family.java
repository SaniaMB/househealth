package com.project.househealth.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "families")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyId;

    @Column(nullable = false, length = 100)
    private String familyName;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Version
    private Long version;

    @OneToMany(
            mappedBy = "family",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FamilyMembership> familyMemberships = new ArrayList<>();

    protected Family(){}

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }

    public Family(String familyName){
        if (familyName == null || familyName.isBlank()) {
            throw new IllegalArgumentException("Family name cannot be empty");
        }
        this.familyName = familyName.trim();
    }

    public Long getFamilyId() {
        return familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<FamilyMembership> getFamilyMemberships() {
        return familyMemberships;
    }

    public void addMembership(FamilyMembership membership) {
        familyMemberships.add(membership);
    }

    public void renameFamily(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Family name cannot be empty");
        }
        this.familyName =  newName.trim();
    }

    @Override
    public String toString() {
        return "Family{" +
                "familyId=" + familyId +
                ", familyName='" + familyName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

}
