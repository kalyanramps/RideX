package com.ridex.ridex.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false,
            unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column
    private String address;

    // ROLE

    @Column
    private String role;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){

        createdAt = LocalDateTime.now();

        if(status == null){

            status = CustomerStatus.ACTIVE;

        }

        // DEFAULT ROLE

        if(role == null){

            role = "CUSTOMER";

        }

    }

   

}
