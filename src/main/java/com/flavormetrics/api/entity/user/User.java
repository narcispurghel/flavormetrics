package com.flavormetrics.api.entity.user;

import com.flavormetrics.api.entity.Authority;
import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import com.flavormetrics.api.exception.impl.MissingAuthorizationElementException;
import com.flavormetrics.api.model.enums.RoleType;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements org.springframework.security.core.userdetails.UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

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

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    @Column(nullable = false)
    private List<Authority> authorities;

    protected <T extends Builder<T>> User(Builder<T> builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.password = builder.password;
        this.isAccountNonExpired = builder.isAccountNonExpired;
        this.isAccountNonLocked = builder.isAccountNonLocked;
        this.isCredentialsNonExpired = builder.isCredentialsNonExpired;
        this.isEnabled = builder.isEnabled;
        this.username = builder.username;
        this.updatedAt = builder.updatedAt;
        this.createdAt = builder.createdAt;
        this.authorities = builder.authorities;
    }

    protected User() {
        // Explicit no args constructor for JPA
    }

    protected static class Builder<T extends Builder<T>> {
        private UUID id;
        private String firstName;
        private String lastName;
        private String password;
        private boolean isAccountNonExpired;
        private boolean isAccountNonLocked;
        private boolean isCredentialsNonExpired;
        private boolean isEnabled;
        private Email username;
        private LocalDateTime updatedAt;
        private final LocalDateTime createdAt = LocalDateTime.now();
        private List<Authority> authorities = new ArrayList<>();

        private Builder() {
            // Prevent instantiation without required properties
        }

        protected Builder(String password, String username, RoleType role)  {
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

        public T id(UUID id) {
            this.id = id;
            return (T) this;
        }

        public T firstName(String firstName) {
            this.firstName = firstName;
            return (T) this;
        }

        public T lastName(String lastName) {
            this.lastName = lastName;
            return (T) this;
        }

        public T accountNonExpired(boolean accountNonExpired) {
            isAccountNonExpired = accountNonExpired;
            return (T) this;
        }

        public T accountNonLocked(boolean accountNonLocked) {
            isAccountNonLocked = accountNonLocked;
            return (T) this;
        }

        public T credentialsNonExpired(boolean credentialsNonExpired) {
            isCredentialsNonExpired = credentialsNonExpired;
            return (T) this;
        }

        public T enabled(boolean enabled) {
            isEnabled = enabled;
            return (T) this;
        }

        public T updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return (T) this;
        }

        public T authorities(List<Authority> authorities) {
            this.authorities = authorities;
            return (T) this;
        }
    }

    /**
     * This method cannot return null because of org.springframework.security.core.userdetails.User contract
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
     * This method cannot return null because of org.springframework.security.core.userdetails.User contract
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
                    "user.username");
        }
        return username.value();
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
