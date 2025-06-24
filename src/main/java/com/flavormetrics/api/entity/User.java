package com.flavormetrics.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(columnDefinition = "boolean not null default true")
    private boolean isAccountNonExpired = true;

    @Column(columnDefinition = "boolean not null default true")
    private boolean isAccountNonLocked = true;

    @Column(columnDefinition = "boolean not null default true")
    private boolean isCredentialsNonExpired = true;

    @Column(columnDefinition = "boolean not null default false")
    private boolean isEnabled = true;

    @Column(name = "updated_at", columnDefinition = "timestamp not null default current_timestamp")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", updatable = false, columnDefinition = "timestamp not null default current_timestamp")
    private final LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id", nullable = false, unique = true)
    private Email email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Recipe> recipes = new HashSet<>();

    public User() {
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = Objects.requireNonNull(passwordHash);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Set<Authority> getAuthorities() {
        return Set.copyOf(authorities);
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = Optional.ofNullable(authorities).map(Set::copyOf).orElse(Set.of());
    }

    public Set<Rating> getRatings() {
        return Set.copyOf(ratings);
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = Optional.ofNullable(ratings).map(Set::copyOf).orElse(Set.of());
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<Recipe> getRecipes() {
        return Set.copyOf(recipes);
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = Optional.ofNullable(recipes).map(Set::copyOf).orElse(Set.of());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) {
            return false;
        }
        return isAccountNonExpired == user.isAccountNonExpired &&
               isAccountNonLocked == user.isAccountNonLocked &&
               isCredentialsNonExpired == user.isCredentialsNonExpired &&
               isEnabled == user.isEnabled &&
               Objects.equals(passwordHash, user.passwordHash) &&
               Objects.equals(firstName, user.firstName) &&
               Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passwordHash, firstName, lastName, isAccountNonExpired,
                isAccountNonLocked, isCredentialsNonExpired, isEnabled);
    }

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
        StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", isAccountNonExpired=").append(isAccountNonExpired);
        sb.append(", isAccountNonLocked=").append(isAccountNonLocked);
        sb.append(", isCredentialsNonExpired=").append(isCredentialsNonExpired);
        sb.append(", isEnabled=").append(isEnabled);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", email=").append(email != null ? email.getAddress() : "null");
        sb.append(", authorities=").append(authorities.size());
        sb.append(", ratings=").append(ratings.size());
        sb.append(", profile=").append(profile == null ? "null" : profile.getId());
        sb.append('}');
        return sb.toString();
    }

}
