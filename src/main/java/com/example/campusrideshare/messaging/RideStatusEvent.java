package com.example.campusrideshare.messaging;

import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.domain.RideStatus;
import com.example.campusrideshare.domain.User;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RideStatusEvent {

    private UUID eventId;
    private UUID rideId;
    private RideStatus status;

    private String departure;
    private String destination;
    private String departureAt;
    private int riderLimit;
    private int passengerCount;

    private String driverName;
    private String driverEmail;

    public RideStatusEvent() {}

    public static RideStatusEvent fromRideStatus(Ride ride,RideStatus rideStatus) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
        RideStatusEvent event = new RideStatusEvent();

        event.setEventId(UUID.randomUUID());
        event.setRideId(ride.getId());
        event.setStatus(rideStatus);
        event.setDeparture(ride.getDeparture());
        event.setDestination(ride.getDestination());
        event.setDepartureAt(ride.getDepartureDate().format(fmt));
        event.setRiderLimit(ride.getRiderLimit());
        event.setPassengerCount(ride.getPassengers().size());

        User driver = ride.getDriver();
        if (driver != null) {
            event.setDriverEmail(driver.getEmail());
            event.setDriverName(driver.getFullName());
        }
        return event;
    }

    public String toStatusEmail(){
        return String.format("""
                RIDE STATUS ALERT
                
                Status: %s
                From: %s
                To: %s
                Departing: %s
                
                Driver: %s
                Seats: %d of %d filled
                
                Ride ID: %s
                
                Best Regards,
                Campus Ride Team
                """,status, departure,destination,departureAt,driverName,passengerCount,riderLimit,rideId);
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getRideId() {
        return rideId;
    }

    public void setRideId(UUID rideId) {
        this.rideId = rideId;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(String departureAt) {
        this.departureAt = departureAt;
    }

    public int getRiderLimit() {
        return riderLimit;
    }

    public void setRiderLimit(int riderLimit) {
        this.riderLimit = riderLimit;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }
}
