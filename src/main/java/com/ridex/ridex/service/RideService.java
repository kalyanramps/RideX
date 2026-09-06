package com.ridex.ridex.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.ridex.ridex.entity.Customer;
import com.ridex.ridex.entity.Driver;
import com.ridex.ridex.entity.Ride;
import com.ridex.ridex.repository.CustomerRepository;
import com.ridex.ridex.repository.DriverRepository;
import com.ridex.ridex.repository.RideRepository;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    public RideService(RideRepository rideRepository,
                       CustomerRepository customerRepository,
                       DriverRepository driverRepository) {

        this.rideRepository = rideRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;

    }

    // GET ALL RIDES

    public List<Ride> getAllRides() {

        return rideRepository.findAll();

    }

    // BOOK RIDE

   // BOOK RIDE

public Ride bookRide(Long customerId,
                     String pickup,
                     String drop,
                     Double distanceKm) {

    Customer customer =
            customerRepository.findById(customerId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Customer not found"
                    )
            );

    // CHECK AVAILABLE DRIVERS

    List<Driver> availableDrivers =
            driverRepository.findByStatus(
                    Driver.DriverStatus.AVAILABLE
            );

    if(availableDrivers.isEmpty()){

        throw new RuntimeException(
                "No drivers available"
        );

    }

    // CREATE RIDE

    Ride ride = new Ride();

    ride.setCustomer(customer);

    // NO DRIVER ASSIGNED INITIALLY

    ride.setDriver(null);

    ride.setPickupLocation(pickup);

    ride.setDropLocation(drop);

    ride.setDistanceKm(distanceKm);

    // FARE

    double fare =
            50 + (distanceKm * 12);

    ride.setFare(fare);

    // OTP

    int otp =
            1000 + new Random().nextInt(9000);

    ride.setOtp(otp);

    // STATUS

    ride.setStatus(
            Ride.RideStatus.REQUESTED
    );

    return rideRepository.save(ride);

}

    // UPDATE STATUS

    public Ride updateRideStatus(Long rideId,
                                 Ride.RideStatus newStatus) {

        Ride ride =
                rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride not found"
                        )
                );

        ride.setStatus(newStatus);

        if(newStatus == Ride.RideStatus.COMPLETED){

    ride.setCompletedAt(
            LocalDateTime.now()
    );

    Driver driver =
            ride.getDriver();

    if(driver != null){

        driver.setStatus(
                Driver.DriverStatus.AVAILABLE
        );

        driverRepository.save(driver);

    }

}

        // AUTO COMPLETE

        /*if(newStatus == Ride.RideStatus.IN_PROGRESS){

            long delaySeconds =
                    (long)(ride.getDistanceKm() * 5);

            new Thread(() -> {

                try {

                    Thread.sleep(delaySeconds * 1000);

                    ride.setStatus(
                            Ride.RideStatus.COMPLETED
                    );

                    ride.setCompletedAt(
                            LocalDateTime.now()
                    );

                    rideRepository.save(ride);

                    // DRIVER AVAILABLE AGAIN

                    Driver driver =
                            ride.getDriver();

                    driver.setStatus(
                            Driver.DriverStatus.AVAILABLE
                    );

                    driverRepository.save(driver);

                    System.out.println(
                            "Ride completed successfully"
                    );

                }

                catch (InterruptedException e){

                    System.out.println(
                            "Ride completion interrupted"
                    );

                }

            }).start();

        }*/

        return rideRepository.save(ride);

    }

    // CANCEL RIDE

    public Ride cancelRide(Long rideId){

        Ride ride =
                rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride not found"
                        )
                );

        // UPDATE STATUS

        ride.setStatus(
                Ride.RideStatus.CANCELLED
        );

        // DRIVER AVAILABLE AGAIN

        if(ride.getDriver() != null){

            Driver driver =
                    ride.getDriver();

            driver.setStatus(
                    Driver.DriverStatus.AVAILABLE
            );

            driverRepository.save(driver);

        }

        return rideRepository.save(ride);

    }

    // ACCEPT RIDE

public Ride acceptRide(Long rideId,
                       Long driverId){

    Ride ride =
            rideRepository.findById(rideId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Ride not found"
                    )
            );

    Driver driver =
            driverRepository.findById(driverId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Driver not found"
                    )
            );

    // CHECK IF ALREADY ACCEPTED

    if(ride.getDriver() != null){

        throw new RuntimeException(
                "Ride already accepted"
        );

    }

    // ASSIGN DRIVER

    ride.setDriver(driver);

    ride.setStatus(
            Ride.RideStatus.ACCEPTED
    );

    // DRIVER BUSY

    driver.setStatus(
            Driver.DriverStatus.ON_TRIP
    );

    driverRepository.save(driver);

    return rideRepository.save(ride);

}

    // GET RIDES BY CUSTOMER

    public List<Ride> getRidesByCustomer(Long customerId) {

        return rideRepository.findByCustomerId(customerId);

    }

    // TOTAL REVENUE

    public Double getTotalRevenue() {

        Double revenue =
                rideRepository.getTotalRevenue();

        return revenue != null
                ? revenue
                : 0.0;

    }

    // RATE DRIVER

    public Ride rateDriver(

        Long rideId,

        Integer rating

){

    Ride ride =
            rideRepository.findById(rideId)
                    .orElseThrow();

    // SAVE RIDE RATING

    ride.setRating(rating);

    rideRepository.save(ride);

    // DRIVER

    Driver driver =
            ride.getDriver();

    // GET ALL COMPLETED RATED RIDES

    List<Ride> completedRides =

            rideRepository.findAll()
                    .stream()
                    .filter(r ->

                            r.getDriver() != null
                            &&

                            r.getDriver().getId()
                                    .equals(driver.getId())

                            &&

                            r.getRating() != null

                    )
                    .toList();

    // CALCULATE AVERAGE

    double total = 0;

    for(Ride r : completedRides){

        total += r.getRating();

    }

    double averageRating =

            total / completedRides.size();

    // UPDATE DRIVER RATING

    driver.setRating(

            Math.round(
                    averageRating * 10.0
            ) / 10.0

    );

    driverRepository.save(driver);

    return ride;

}

    // PAYMENT

    public Ride makePayment(Long rideId,
                            String paymentMethod){

        Ride ride =
                rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride not found"
                        )
                );

        ride.setPaid(true);

        ride.setPaymentMethod(
                paymentMethod
        );

        return rideRepository.save(ride);

    }

}