package com.ridex.ridex.repository;

import com.ridex.ridex.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Get all rides by customer
    List<Ride> findByCustomerId(Long customerId);

    // Get all rides by driver
    List<Ride> findByDriverId(Long driverId);

    // Get rides by status
    List<Ride> findByStatus(Ride.RideStatus status);

    // Count rides by status
    long countByStatus(Ride.RideStatus status);

    // Get total revenue from completed rides
    @Query("SELECT SUM(r.fare) FROM Ride r WHERE r.status = 'COMPLETED'")
    Double getTotalRevenue();
}