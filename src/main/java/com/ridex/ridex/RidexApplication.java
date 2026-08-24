package com.ridex.ridex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ridex.ridex.entity.Driver;
import com.ridex.ridex.repository.DriverRepository;

@SpringBootApplication
public class RidexApplication implements CommandLineRunner {

    @Autowired
    private DriverRepository driverRepository;

    public static void main(String[] args) {

        SpringApplication.run(
                RidexApplication.class,
                args
        );

    }

    @Override
    public void run(String... args) throws Exception {

        // AVOID DUPLICATES

        if(driverRepository.count() > 0){

            System.out.println(
                    "Drivers Already Exist!"
            );

            return;

        }

        // CREATE 20 DRIVERS

        for(int i = 1; i <= 20; i++){

            Driver driver = new Driver();

            // DRIVER DETAILS

            driver.setFullName(
                    "Driver " + i
            );

            driver.setEmail(
                    "driver" + i + "@ridex.com"
            );

            driver.setPhone(
                    "98765432" + i
            );

            // PASSWORD

            driver.setPassword(
                    "driver123"
            );

            // LICENSE

            driver.setLicenseNumber(
                    "LIC" + (1000 + i)
            );

            // VEHICLE TYPES

            if(i % 3 == 0){

                driver.setVehicleModel(
                        "Bike Taxi"
                );

            }

            else if(i % 2 == 0){

                driver.setVehicleModel(
                        "Auto Rickshaw"
                );

            }

            else{

                driver.setVehicleModel(
                        "Sedan Cab"
                );

            }

            // VEHICLE NUMBER

            driver.setVehicleNumber(
                    "KA01AB" + (100 + i)
            );

            // RATING

            driver.setRating(4.5);

            // STATUS

            driver.setStatus(
                    Driver.DriverStatus.AVAILABLE
            );

            // SAVE DRIVER

            driverRepository.save(driver);

        }

        System.out.println(
                "20 Drivers Added Successfully!"
        );

    }

}