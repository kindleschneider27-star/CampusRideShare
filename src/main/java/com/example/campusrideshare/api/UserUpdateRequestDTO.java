package com.example.campusrideshare.api;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequestDTO {
    @NotBlank(message="A name is required")
    private String fullName;
    @NotBlank(message="An email is required")
    private String email;
    @NotBlank(message="A department is required")
    private String department;

    public UserUpdateRequestDTO() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
