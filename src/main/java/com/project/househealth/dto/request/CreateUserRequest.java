package com.project.househealth.dto.request;

public class CreateUserRequest {

    private String name;
    private String email;
    private String password;

    public CreateUserRequest() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
