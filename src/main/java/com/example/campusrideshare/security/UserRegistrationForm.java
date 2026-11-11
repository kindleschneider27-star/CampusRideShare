package com.example.campusrideshare.security;

import com.example.campusrideshare.domain.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistrationForm {
    @NotBlank(message = "")
    @Size(min=5, max=50, message="Username must be between 5 and 50 character.")
    private String username;
    @NotBlank(message="A password is required.")
    @Size(min=8, message="Password must be at least 8 characters long.")
    private String password;
    @NotBlank(message="Please confirm your password")
    private String confirmPassword;
    @NotBlank(message="Your full name is required.")
    private String fullName;
    @NotBlank(message="Your email is required.")
    @Email(message="Enter a valid email address.")
    private String email;
    @NotNull(message = "Department required, if you dont have one select Undecided.")
    private Department department;

    public UserRegistrationForm() {
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
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
