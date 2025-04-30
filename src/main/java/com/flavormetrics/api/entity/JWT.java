package com.flavormetrics.api.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "jwt", schema = "users")
public class JWT {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public JWT() {
        // Explicit now args constructor for JPA
    }

    private JWT(Builder builder) {
        this.id = builder.id;
    }

    public static class Builder {
        private UUID id;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public JWT build() {
            return new JWT(this);
        }
    }

    public UUID id() {
        return id;
    }
}