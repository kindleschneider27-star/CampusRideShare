package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.domain.User;

public class DriverResponseDTO {
    private String fullName;
    private String email;
    private Department department;


    public static DriverResponseDTO from(User user) {
        if (user == null) {
            return null;
        }

        DriverResponseDTO response = new DriverResponseDTO();

        response.setFullName(user.getFullName());

        return response;

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
