package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.services.RideService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    public Page<RideResponseDTO> searchRides(@RequestParam(defaultValue = "")String name,
                                                 @RequestParam(required = false)AnimalType type,
                                                 @RequestParam(defaultValue = "")String subtype) {



        return rideService.searchRides(name, type, subtype).map(RideResponseDTO::from);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<RideResponseDTO> getRide(@PathVariable UUID id,
                                                       @AuthenticationPrincipal Jwt jwt) {
        Optional<Ride> ride = rideService.getRideById(id);

        if (ride.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean includeOwner = jwt != null;

        RideResponseDTO response = RideResponseDTO.from(ride.get(), includeOwner);

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes="application/json")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<RideResponseDTO> createAnimal(@Valid @RequestBody RideCreateRequestDTO request,
                                                          @AuthenticationPrincipal Jwt jwt) {

        Ride ride = RideRequestMapper.fromCreate(request);

        String username = jwt.getSubject();


        Ride savedRide = rideService.registerNewRide(ride, username);


        RideResponseDTO response = RideResponseDTO.from(savedRide, true);


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(path="/{id}", consumes = "application/json")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<RideResponseDTO> patchRide(@PathVariable UUID id,
                                                         @RequestBody RidePatchRequestDTO request) {

        Optional<Ride> updatedRide = rideService.patchUpdate(id, request);

        if (updatedRide.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RideResponseDTO response = RideResponseDTO.from(updatedRide.get(), true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<Void> deleteRide(@PathVariable UUID id) {
        boolean deleted = rideService.deleteRideById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


}
