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

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY)
    private List<FamilyMembership> familyMemberships = new ArrayList<>();

    protected Family(){}

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }

    public Family(String familyName){
        this.familyName = familyName;
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

    public List<FamilyMembership> getFamilyMemberships() {
        return familyMemberships;
    }

    public void renameFamily(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Family name cannot be empty");
        }
        this.familyName = newName;
    }
}
