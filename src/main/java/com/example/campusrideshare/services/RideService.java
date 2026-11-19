package com.example.campusrideshare.services;

import com.example.campusrideshare.data.RideRepository;
import com.example.campusrideshare.data.UserRepository;
import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.domain.RideStatus;
import com.example.campusrideshare.domain.User;
import com.example.campusrideshare.messaging.RideStatusEvent;
import com.example.campusrideshare.messaging.RideStatusEventProducer;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RideService {
    private static final Logger logger = LoggerFactory.getLogger(RideService.class);
    private final RideRepository rideRepo;
    private final UserRepository userRepo;
    private final RideStatusEventProducer producer;

    public RideService(RideRepository rr, UserRepository ur, RideStatusEventProducer producer) {
        this.rideRepo = rr;
        this.userRepo = ur;
        this.producer = producer;
    }

    public List<Ride> getAllRides() {
        return rideRepo.findAllByOrderByDepartureDateAsc();
    }
    public Optional<Ride> getRideById(UUID id) {
        return rideRepo.findById(id);
    }
    public List<Ride> getRidesByDriver(UUID driverId) {
        return rideRepo.findByDriver_Id(driverId);
    }
    public List<Ride> getRidesByDriver_Department (Department dept) {
        return rideRepo.findByDriver_Department(dept);
    }
    

    public Ride saveRideForUser(Ride ride, String username) {
        User driver = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));

        ride.setStatus(RideStatus.OPEN);
        ride.setDriver(driver);
        Ride saved = rideRepo.save(ride);
        logger.info("Ride registered to {}: {}", username, saved);
        return saved;
    }

    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    public boolean deleteRideById(UUID id) {
        Optional<Ride> ride = rideRepo.findById(id);
        if (ride.isEmpty()) { return false; }
        rideRepo.delete(ride.get());
        return true;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    public boolean updateRide (UUID id, Ride ride) {
        Optional<Ride> rideOpt = rideRepo.findById(id);
        if (rideOpt.isEmpty()) {
            logger.info("Error saving ride with ID: {} doesnt exist", id);
            return false;
        }
        Ride saved = rideOpt.get();

        ride.setId(id);
        ride.setDriver(saved.getDriver());
        ride.setStatus(saved.getStatus());

        rideRepo.save(ride);
        logger.info("Ride updated to {}: {}", id, ride);
        return true;
    }

    @Transactional
    public Ride registerNewRide(Ride ride, String username) {
        Optional<User> driver = userRepo.findByUsername(username);
        if (driver.isEmpty()) {
            return null;
        }
        ride.setDriver(driver.get());
        ride.setStatus(RideStatus.OPEN);
        Ride saved = rideRepo.save(ride);
        publishStatus(saved, RideStatus.OPEN);
        return saved;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    public Ride changeStatus(UUID id, RideStatus status) {
        Optional<Ride> rideOpt = rideRepo.findById(id);
        if (rideOpt.isEmpty()) {
            return null;
        }
        Ride ride = rideOpt.get();
        ride.setStatus(status);
        Ride saved = rideRepo.save(ride);
        publishStatus(saved, status);
        logger.info("Ride {} status changed to {}", id, status);
        return saved;
    }

    private void publishStatus(Ride ride, RideStatus status) {
        try {
            producer.send(RideStatusEvent.fromRideStatus(ride, status));
        } catch (Exception ex) {
            logger.error("Failed to publish status event for ride {}: {}", ride.getId(), ex.getMessage());
        }
    }

    @Transactional
    public Ride joinRide(UUID rideId, String username) {
        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));
        User passenger = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (ride.getStatus() != RideStatus.OPEN) {
            throw new IllegalStateException("This ride is " + ride.getStatus() + " and is not taking passengers.");
        }
        if (ride.getDriver().getUsername().equals(username)) {
            throw new IllegalStateException("You are the driver of this ride.");
        }
        if (ride.getPassengers().contains(passenger)) {
            throw new IllegalStateException("You have already joined this ride.");
        }
        if (ride.getPassengers().size() >= ride.getRiderLimit()) {
            throw new IllegalStateException("This ride is already full.");
        }

        ride.getPassengers().add(passenger);
        if (ride.getPassengers().size() >= ride.getRiderLimit()) {
            ride.setStatus(RideStatus.FULL);
        }

        Ride saved = rideRepo.save(ride);
        logger.info("Passenger {} joined ride {} ({}/{} seats)", username, rideId,
                saved.getPassengers().size(), saved.getRiderLimit());
        publishStatus(saved, saved.getStatus());
        return saved;
    }

}
