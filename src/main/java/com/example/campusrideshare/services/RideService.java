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
    public Ride joinRide(UUID rideId, String username) throws IllegalStateException {
        Optional<Ride> rideJoin = rideRepo.findById(rideId);
        if (rideJoin.isEmpty()) {
            logger.error("Error joining ride with ID {}", rideId);
            return null;
        }
        Ride ride = rideJoin.get();
        if (ride.getStatus() != RideStatus.OPEN) {
            logger.error("Ride with ID {} is not open", rideId);
            return null;
        }
        if (ride.getDriver().getUsername().equals(username)) {
            logger.error("Ride with ID {} is already joined", rideId);
            return null;
        }
        if(ride.getPassengers().contains(username)) {
            logger.error("Ride with ID {} is already joined", rideId);
            return null;
        }
        if(ride.getPassengers().size() == rideJoin.get().getRiderLimit()){
            logger.error("Ride with ID {} is at capacity", rideId);
            return null;
        }
        ride.getPassengers().add(ride.getUserusername);

        if(rideJoin.get().getPassengers().size() == rideJoin.get().getRiderLimit()){
            logger.error("Ride with ID {} is now at capacity", rideId);
            ride.setStatus(RideStatus.FULL);
        }
        publishStatus(ride, ride.getStatus());

        return rideJoin.get();

    }

}
