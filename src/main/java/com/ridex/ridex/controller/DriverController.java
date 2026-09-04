package com.ridex.ridex.controller;

import com.ridex.ridex.entity.Driver;
import com.ridex.ridex.service.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@CrossOrigin("*")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    // Get all drivers
    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    // Get driver by ID
    @GetMapping("/{id}")
    public Driver getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));
    }

    // Add new driver
    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return driverService.addDriver(driver);
    }

    // Update driver
    @PutMapping("/{id}")
    public Driver updateDriver(@PathVariable Long id,
                               @RequestBody Driver driver) {
        return driverService.updateDriver(id, driver);
    }

    // Delete driver
    @DeleteMapping("/{id}")
    public String deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return "Driver deleted successfully!";
    }

    // Get available drivers
    @GetMapping("/available")
    public List<Driver> getAvailableDrivers() {
        return driverService.getAvailableDrivers();
    }

    // Count available drivers
    @GetMapping("/count/available")
    public long countAvailableDrivers() {
        return driverService.countAvailableDrivers();
    }
}
