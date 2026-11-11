package com.example.campusrideshare.data;

import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {
    List<Ride> findAllByOrderByDepartureDateAsc();
    List<Ride> findByDriver_Id(UUID driverId);
    List<Ride> findByDriver_Department(Department department);

}
