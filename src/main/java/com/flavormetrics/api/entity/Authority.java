package com.flavormetrics.api.entity;

import com.flavormetrics.api.entity.user.User;
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
    @JoinColumn(name = "user_id")
    private User user;

    public Authority() {
        // Explicit no args constructor for JPA
    }

    private Authority(Builder builder) {
        this.id = builder.id;
        this.role = builder.role;
    }

    public static class Builder {
        private UUID id;
        private RoleType role;
        private User user;

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

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
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

    @Override
    public String getAuthority() {
        return role.name();
    }

    public User getUser() {
        return user;
    }
}
