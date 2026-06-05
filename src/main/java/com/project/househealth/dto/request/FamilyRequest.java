package com.project.househealth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FamilyRequest {

    @NotBlank(message = "Family name is required")
    @Size(min = 2, max = 100, message = "Family name must be between 2 and 100 characters")
    private String familyName;

    public FamilyRequest() {}

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

}
