package com.example.campusrideshare.api;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public class RideCreateRequestDTO {

    @NotNull(message = "A rider limit is required")
    @Min(value = 1, message = "At least 1 rider is required")
    @Max(value = 6, message = "No more than 6 riders")
    private Integer riderLimit;
    @NotBlank(message="A destination location is required")
    private String destination;
    @NotBlank(message="A departure location is required")
    private String departure;

    @NotNull(message="A departure date and time is required")
    private String departureDate;


    public RideCreateRequestDTO() {}


    public int getRiderLimit() {
        return riderLimit;
    }

    public void setRiderLimit(int riderLimit) {
        this.riderLimit = riderLimit;
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

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

}
