package com.example.campusrideshare.services;

import com.example.campusrideshare.data.RideRepository;
import com.example.campusrideshare.data.UserRepository;
import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.domain.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RideService {
    private static final Logger logger = LoggerFactory.getLogger(RideService.class);
    private final RideRepository rideRepo;
    private final UserRepository userRepo;

    public RideService(RideRepository rr, UserRepository ur) {
        this.rideRepo = rr;
        this.userRepo = ur;
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

        ride.setDriver(driver);
        Ride saved = rideRepo.save(ride);
        logger.info("Ride registered to {}: {}", username, saved);
        return saved;
    }


}
