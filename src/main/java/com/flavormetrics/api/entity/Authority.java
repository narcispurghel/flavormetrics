package com.flavormetrics.api.entity;

import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import com.flavormetrics.api.model.enums.RoleType;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Entity
@Table(name = "authority", schema = "users")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER;

    @ManyToOne
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;

    public Authority() {
        // Explicit no args constructor for JPA
    }

    private Authority(Builder builder) {
        this.id = builder.id;
        this.role = builder.role;
        this.userDetails = builder.userDetails;
    }

    public static class Builder {

        private UUID id;
        private final RoleType role;
        private UserDetails userDetails;

        public Builder(RoleType role) {

            if (role == null) {
                throw new InvalidArgumentException(
                        "Invalid role.type",
                        "type must be a non-null value",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "role.type"
                );
            }

            this.role = role;
        }

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder setUserDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
            return this;
        }

        public Authority build() {
            return new Authority(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public RoleType getRole() {
        return role;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }
}
