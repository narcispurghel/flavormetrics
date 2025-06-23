package com.flavormetrics.api.entity;

import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DietaryPreferenceType dietaryPreference = DietaryPreferenceType.NONE;

    @Column(name = "updated_at", columnDefinition = "timestamp not null default current_timestamp")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", updatable = false, columnDefinition = "timestamp not null default current_timestamp")
    private final LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id"))
    private Set<Allergy> allergies = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Profile() {
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DietaryPreferenceType getDietaryPreference() {
        return dietaryPreference;
    }

    public void setDietaryPreference(
            DietaryPreferenceType dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
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

    public Set<Allergy> getAllergies() {
        return new HashSet<>(allergies);
    }

    public void setAllergies(Set<Allergy> allergies) {
        this.allergies = new HashSet<>(allergies);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profile profile)) {
            return false;
        }
        return dietaryPreference == profile.dietaryPreference &&
               Objects.equals(updatedAt, profile.updatedAt) &&
               Objects.equals(createdAt, profile.createdAt) &&
               Objects.equals(allergies, profile.allergies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dietaryPreference, updatedAt, createdAt, allergies);
    }

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
        StringBuilder sb = new StringBuilder("Profile{");
        sb.append("id=").append(id);
        sb.append(", dietaryPreference=").append(dietaryPreference);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", allergies=").append(allergies);
        sb.append(", user=").append(user == null ? "null" : user.getId());
        sb.append('}');
        return sb.toString();
    }

}
