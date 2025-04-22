package com.flavormetrics.api.entity;

import com.flavormetrics.api.enums.DietaryPreferenceType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profile", schema = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DietaryPreferenceType dietaryPreference = DietaryPreferenceType.NONE;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allergy> allergies = new ArrayList<>();

    @OneToOne(mappedBy = "profile")
    private User user;

    public Profile() {}

    public Profile(DietaryPreferenceType dietaryPreference, List<Allergy> allergies, User user) {
        this.dietaryPreference = dietaryPreference;
        this.allergies = allergies;
        this.user = user;
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

    public void setDietaryPreference(DietaryPreferenceType dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
