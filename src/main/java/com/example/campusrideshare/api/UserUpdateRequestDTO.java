package com.example.campusrideshare.api;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequestDTO {
    @NotBlank(message="A name is required")
    private String fullName;
    @NotBlank(message="An email is required")
    private String Email;
    @NotBlank(message="A department is required")
    private String Department;

    public UserUpdateRequestDTO() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
