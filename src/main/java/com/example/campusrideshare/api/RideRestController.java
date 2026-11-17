package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.services.RideService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/rides", produces = "application/json")
@CrossOrigin(origins="http://localhost:3000")
public class RideRestController {
    private final RideService rideService;


    public RideRestController(RideService rs) {
        rideService = rs;
    }

    @GetMapping("/search")
    public List<RideResponseDTO> getRides() {
        return rideService.getAllRides().stream()
                .map(RideResponseDTO::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDTO> getRide(@PathVariable UUID id,
                                                   @AuthenticationPrincipal Jwt jwt) {
        Optional<Ride> ride = rideService.getRideById(id);

        if (ride.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean includeDriver = jwt != null;

        return ResponseEntity.ok(RideResponseDTO.from(ride.get(), includeDriver));
    }

    @PostMapping(consumes = "application/json")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RideResponseDTO> createRide(@Valid @RequestBody RideCreateRequestDTO request,
                                                      @AuthenticationPrincipal Jwt jwt) {
        Ride ride = RideRequestMapper.fromCreate(request);

        String username = jwt.getSubject();

        Ride saved = rideService.registerNewRide(ride, username);

        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(RideResponseDTO.from(saved, true));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    public ResponseEntity<Void> deleteRide(@PathVariable UUID id) {
        boolean deleted = rideService.deleteRideById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }



}
