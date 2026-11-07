package com.example.campusrideshare.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @NotNull(message ="A driver is required")
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
    private LocalDateTime departureDate;
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    public Ride() {}

    public Ride(User driver, int riderLimit, List<User> passengers, String destination, String departure, LocalDateTime departureDate, RideStatus status) {
        this.driver = driver;
        this.riderLimit = riderLimit;
        this.passengers = passengers;
        this.destination = destination;
        this.departure = departure;
        this.departureDate = departureDate;
        this.status = status;
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

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", driver=" + driver +
                ", riderLimit=" + riderLimit +
                ", passengers=" + passengers +
                ", destination='" + destination + '\'' +
                ", departure='" + departure + '\'' +
                ", departureDate=" + departureDate +
                ", status=" + status +
                '}';
    }
}
