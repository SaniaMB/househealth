package com.project.househealth.dto.response;

import java.time.Instant;

public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private Instant createdAt;

    public UserResponse(Long userId,
                        String name,
                        String email,
                        Instant createdAt) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

}
