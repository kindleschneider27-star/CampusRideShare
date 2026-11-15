package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Ride;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;

import static org.springframework.security.web.savedrequest.FastHttpDateFormat.parseDate;

public class RideRequestMapper {

    private RideRequestMapper() {
    }

    public static Ride fromCreate(RideCreateRequestDTO request) {
        Ride ride = new Ride();

        ride.setDestination(request.getDestination());
        ride.setDeparture(request.getDeparture());
        ride.setRiderLimit(request.getRiderLimit());
        ride.setDepartureDate(parseDate(request.getDepartureDate()));

        return ride;
    }

    public static LocalDateTime parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

}
