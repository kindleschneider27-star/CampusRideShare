package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.domain.RideStatus;
import com.example.campusrideshare.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RideResponseDTO {

    private UUID id;
    private User driver;
    private int riderLimit;
    private List<User> passengers = new ArrayList<>();
    private String destination;
    private String departure;
    private LocalDateTime departureDate;
    private RideStatus status;
    private UserResponseDTO user;

    public Ride() {}

    public static RideResponseDTO from(Ride ride) {
        RideResponseDTO response = new RideResponseDTO();
        response.setId(ride.getId());
        response.setDriver(ride.getDriver());
        response.setRiderLimit(ride.getRiderLimit());
        response.setPassengers(ride.getPassengers());
        response.setDestination(ride.getDestination());
        response.setDeparture(ride.getDeparture());
        response.setDepartureDate(ride.getDepartureDate());
        response.setStatus(ride.getStatus());
        return response;
    }
    public static RideResponseDTO from(Ride ride) {
        return from(ride, false);
    }
    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public int getRiderLimit() {
        return riderLimit;
    }

    public void setRiderLimit(int riderLimit) {
        this.riderLimit = riderLimit;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<User> passengers) {
        this.passengers = passengers;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }
}
