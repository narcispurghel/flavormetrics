package com.flavormetrics.api.entity;

import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import com.flavormetrics.api.exception.impl.MissingAuthorizationElementException;
import com.flavormetrics.api.model.enums.RoleType;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_details", schema = "users")
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private boolean isAccountNonExpired;

    @Column(nullable = false)
    private boolean isAccountNonLocked;

    @Column(nullable = false)
    private boolean isCredentialsNonExpired;

    @Column(nullable = false)
    private boolean isEnabled;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_email_id", nullable = false)
    private Email username;

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<Authority> authorities = new ArrayList<>();

    public UserDetails() {
        // Explicit no args constructor for JPA
    }

    private UserDetails(Builder builder) {
        this.id = builder.id;
        this.isAccountNonExpired = builder.isAccountNonExpired;
        this.isAccountNonLocked = builder.isAccountNonLocked;
        this.isCredentialsNonExpired = builder.isCredentialsNonExpired;
        this.isEnabled = builder.isEnabled;
        this.password = builder.password;
        this.username = builder.username;
        this.authorities = builder.authorities;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public static class Builder {

        private UUID id;
        private boolean isAccountNonExpired = true;
        private boolean isAccountNonLocked = true;
        private boolean isCredentialsNonExpired = true;
        private boolean isEnabled = true;
        private String password;
        private Email username;
        private List<Authority> authorities = new ArrayList<>();

        public Builder(String password, String username, RoleType role) {

            if (password == null || password.isBlank()) {
                throw new InvalidArgumentException(
                        "Invalid password",
                        "password must be non-null and non-blank",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "userDetails.password"
                );
            }

            if (username == null || username.isBlank()) {
                throw new InvalidArgumentException(
                        "Invalid username",
                        "username must be non-null and non-blank",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "userDetails.username"
                );
            }

            this.password = password;
            this.username = new Email.Builder(username)
                    .build();
            this.authorities.add(new Authority.Builder(role)
                    .build());
        }

        public Builder accountNonExpired(boolean accountNonExpired) {
            isAccountNonExpired = accountNonExpired;
            return this;
        }

        public Builder accountNonLocked(boolean accountNonLocked) {
            isAccountNonLocked = accountNonLocked;
            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired) {
            isCredentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder enabled(boolean enabled) {
            isEnabled = enabled;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder username(Email username) {
            this.username = username;
            return this;
        }

        public Builder authorities(List<Authority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserDetails build() {
            return new UserDetails(this);
        }
    }

    /**
     * This method cannot return null because of org.springframework.security.core.userdetails.UserDetails contract
     * Additional check for empty is implemented for authentication/authorization
     *
     * @return a collection of Authority
     */
    @Override
    public List<Authority> getAuthorities() {

        if (authorities == null || authorities.isEmpty()) {
            throw new MissingAuthorizationElementException(
                    "Invalid authorities",
                    "authorities should be a non-null and non-empty value",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "user.authorities"
            );
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * This method cannot return null because of org.springframework.security.core.userdetails.UserDetails contract
     * Additional check for empty is implemented for authentication/authorization
     *
     * @return a populated String
     */
    @Override
    public String getUsername() {

        if (username == null) {
            throw new MissingAuthorizationElementException(
                    "Invalid username",
                    "username should be a non-null and non-blank value",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "user.username"
            );
        }

        return username.getValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
