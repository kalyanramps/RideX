package com.ridex.ridex.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id",
                nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String dropLocation;

    @Column
    private Double distanceKm;

    @Column
    private Double fare;

    @Column
    private Integer otp;

    @Column
    private Integer rating;

    // PAYMENT

    @Column
    private Boolean paid;

    @Column
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @Column
    private LocalDateTime bookedAt;

    @Column
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist(){

        bookedAt = LocalDateTime.now();

        if(status == null){

            status = RideStatus.REQUESTED;

        }

        if(paid == null){

            paid = false;

        }

    }
}