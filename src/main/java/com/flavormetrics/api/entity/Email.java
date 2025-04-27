package com.flavormetrics.api.entity;

import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Entity
@Table(name = "email", schema = "users")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String value;

    @OneToOne(mappedBy = "username")
    private UserDetails userDetails;

    public Email() {
        // Explicit no args constructor for JPA
    }

    private Email(Builder builder) {
        this.value = builder.value;
    }

    public static class Builder {

        private String value;
        private UserDetails userDetails;

        public Builder(String value) {

            if (value == null || value.isBlank()) {
                throw new InvalidArgumentException(
                        "Invalid value",
                        "value must be non-null and non-blank",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "email.value"
                );
            }

            this.value = value;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setUserDetails(UserDetails value) {
            this.userDetails = value;
            return this;
        }


        public Email build() {
            return new Email(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }
}
