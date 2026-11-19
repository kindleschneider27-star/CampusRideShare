package com.example.campusrideshare.api;

import jakarta.validation.constraints.NotBlank;

public class UserCreateRequestDTO {
    @NotBlank(message="A username is required")
    private String username;
    @NotBlank(message="A password is required")
    private String password;
    @NotBlank(message="A confirm password is required")
    private String confirmPassword;
    @NotBlank(message="A name is required")
    private String fullName;
    @NotBlank(message="An email is required")
    private String Email;
    @NotBlank(message="A department is required")
    private String Department;

    public UserCreateRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
