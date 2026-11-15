package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.domain.RideStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RideResponseDTO {

    private UUID id;
    private DriverResponseDTO driver;
    private int riderLimit;
    private List<UserResponseDTO> passengers = new ArrayList<>();
    private int passengerCount;
    private String destination;
    private String departure;
    private LocalDateTime departureDate;
    private RideStatus status;


    public RideResponseDTO() {}

    public static RideResponseDTO from(Ride ride, boolean includeDriver) {
        RideResponseDTO response = new RideResponseDTO();
        response.setId(ride.getId());
        response.setRiderLimit(ride.getRiderLimit());
        response.setDestination(ride.getDestination());
        response.setDeparture(ride.getDeparture());
        response.setDepartureDate(ride.getDepartureDate());
        response.setStatus(ride.getStatus());

        int count = ride.getPassengers() == null ? 0 : ride.getPassengers().size();
        response.setPassengerCount(count);

        if (includeDriver) {
            response.setDriver(DriverResponseDTO.from(ride.getDriver()));
            if (ride.getPassengers() != null) {
                ride.getPassengers().forEach(p -> response.getPassengers().add(UserResponseDTO.from(p)));
            }
        } else {
            response.setDriver(null);
        }
        return response;
    }
    public static RideResponseDTO from(Ride ride) {
        return from(ride, false);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DriverResponseDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverResponseDTO driver) {
        this.driver = driver;
    }

    public int getRiderLimit() {
        return riderLimit;
    }

    public void setRiderLimit(int riderLimit) {
        this.riderLimit = riderLimit;
    }

    public List<UserResponseDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserResponseDTO> passengers) {
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

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }
}
