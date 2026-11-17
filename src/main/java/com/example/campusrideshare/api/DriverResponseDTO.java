package com.example.campusrideshare.api;


import com.example.campusrideshare.domain.User;

import java.util.UUID;

public class DriverResponseDTO {
    private UUID id;
    private String username;
    private String fullName;
    private String department;


    public static DriverResponseDTO from(User user) {
        if (user == null) {
            return null;
        }

        DriverResponseDTO response = new DriverResponseDTO();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setDepartment(user.getDepartment() == null ? null : user.getDepartment().name());


        return response;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


}
