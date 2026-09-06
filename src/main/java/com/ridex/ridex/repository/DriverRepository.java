package com.ridex.ridex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ridex.ridex.entity.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    // FIND AVAILABLE DRIVERS

    List<Driver> findByStatus(
            Driver.DriverStatus status
    );

    // SEARCH DRIVER

    List<Driver> findByFullNameContainingIgnoreCase(
            String name
    );

    // FIND LICENSE

    Driver findByLicenseNumber(
            String licenseNumber
    );

    // FIND DRIVER BY EMAIL

    Optional<Driver> findByEmail(
            String email
    );

    // COUNT DRIVERS

    long countByStatus(
            Driver.DriverStatus status
    );

}