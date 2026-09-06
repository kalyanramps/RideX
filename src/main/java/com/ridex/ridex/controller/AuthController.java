package com.ridex.ridex.controller;

import com.ridex.ridex.dto.LoginRequest;
import com.ridex.ridex.dto.LoginResponse;
import com.ridex.ridex.entity.Customer;
import com.ridex.ridex.entity.Driver;
import com.ridex.ridex.repository.CustomerRepository;
import com.ridex.ridex.repository.DriverRepository;
import com.ridex.ridex.security.JwtUtil;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final CustomerRepository customerRepository;

    private final DriverRepository driverRepository;

    public AuthController(
            CustomerRepository customerRepository,
            DriverRepository driverRepository
    ){

        this.customerRepository =
                customerRepository;

        this.driverRepository =
                driverRepository;

    }

    // CUSTOMER / ADMIN LOGIN

    @PostMapping("/login")

    public LoginResponse login(
            @RequestBody LoginRequest request
    ){

        Customer customer =
                customerRepository
                .findByEmail(
                        request.getEmail()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid Email"
                        )
                );

        // PASSWORD CHECK

        if(
                !customer.getPassword()
                .equals(
                        request.getPassword()
                )
        ){

            throw new RuntimeException(
                    "Invalid Password"
            );

        }

        // GENERATE TOKEN

        String token =
                JwtUtil.generateToken(
                        customer.getEmail()
                );

        return new LoginResponse(

                token,

                customer.getRole(),

                customer.getId(),

                customer.getFullName()

        );

    }

    // DRIVER LOGIN

    @PostMapping("/driver/login")

    public LoginResponse driverLogin(
            @RequestBody LoginRequest request
    ){

        Driver driver =
                driverRepository
                .findByEmail(
                        request.getEmail()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver not found"
                        )
                );

        // PASSWORD CHECK

        if(
                !driver.getPassword()
                .equals(
                        request.getPassword()
                )
        ){

            throw new RuntimeException(
                    "Invalid Password"
            );

        }

        // TOKEN

        String token =
                JwtUtil.generateToken(
                        driver.getEmail()
                );

        return new LoginResponse(

                token,

                "DRIVER",

                driver.getId(),

                driver.getFullName()

        );

    }

}
