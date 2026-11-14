package com.example.campusrideshare.security;

import com.example.campusrideshare.data.RideRepository;
import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.services.RideService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RideAuthorization {
    private final RideService rideService;

    public RideAuthorization(RideService rideService) {
        this.rideService = rideService;
    }

    public boolean isOwner(UUID rideId, String username) {
        return rideService.getRideById(rideId)
                .map(r -> r.getDriver().getUsername().equals(username))
                .orElse(false);
    }
}
