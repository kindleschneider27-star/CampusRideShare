package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.RideStatus;
import com.example.campusrideshare.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class RideCreateRequestDTO {
    @ManyToOne
    private User driver;
    @Min(1) @Max(6)
    private int riderLimit;
    @ManyToMany
    private List<User> passengers = new ArrayList<>();
    @NotBlank(message="A destination location is required")
    private String destination;
    @NotBlank(message="A departure location is required")
    private String departure;
    @NotNull(message="A departure date and time is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureDate;
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    public RideCreateRequestDTO() {}


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
