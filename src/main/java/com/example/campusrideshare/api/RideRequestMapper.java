package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Ride;

import java.util.Base64;

public class RideRequestMapper {
    private RideRequestMapper() {

    }

    public static Ride fromCreate(RideCreateRequestDTO request) {
        Ride ride = new Ride();

        ride.setName(request.getName());


        return ride;
    }

}
