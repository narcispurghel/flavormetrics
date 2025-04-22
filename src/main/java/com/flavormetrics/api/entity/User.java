package com.flavormetrics.api.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user", schema = "profile")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
