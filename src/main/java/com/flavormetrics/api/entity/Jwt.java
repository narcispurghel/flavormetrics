package com.flavormetrics.api.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "jwt", schema = "users")
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public Jwt() {
        // Explicit now args constructor for JPA
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}