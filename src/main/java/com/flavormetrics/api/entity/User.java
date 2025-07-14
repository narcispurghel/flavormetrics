package com.flavormetrics.api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import com.flavormetrics.api.exception.impl.MissingAuthorizationElementException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private boolean isAccountNonExpired = true;
    
    @Column(nullable = false)
    private boolean isAccountNonLocked = true;
    
    @Column(nullable = false)
    private boolean isCredentialsNonExpired = true;
    
    @Column(nullable = false)
    private boolean isEnabled = true;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "user",
               cascade = CascadeType.PERSIST,
               fetch = FetchType.EAGER)
    private List<Authority> authorities = new ArrayList<>();
    
    @OneToMany(mappedBy = "user")
    private List<Rating> ratings = new ArrayList<>();
    
    protected User() {
    }
    
    /**
     * This method cannot return null because of
     * org.springframework.security.core.userdetails.User contract
     *
     * @return a collection of Authority
     */
    @Override
    public List<Authority> getAuthorities() {
        if (authorities == null) {
            throw new MissingAuthorizationElementException("Invalid " +
                                                           "authorities",
                                                           "authorities " +
                                                           "should be a " +
                                                           "non-null and " +
                                                           "non-empty value",
                                                           HttpStatus.INTERNAL_SERVER_ERROR,
                                                           "user.authorities");
        }
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    /**
     * This method cannot return null because of
     * org.springframework.security.core.userdetails.User contract
     */
    @Override
    public String getUsername() {
        if (email == null) {
            throw new MissingAuthorizationElementException("Invalid username",
                                                           "username should " +
                                                           "be a non-null and" +
                                                           " non-blank value",
                                                           HttpStatus.INTERNAL_SERVER_ERROR,
                                                           "user" +
                                                           ".username");
        }
        return email;
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
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setAccountNonExpired(boolean isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }
    
    public void setAccountNonLocked(boolean isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }
    
    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }
    
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
    
    public List<Rating> getRatings() {
        return ratings;
    }
    
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
