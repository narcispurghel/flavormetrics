package com.flavormetrics.api.entity;

import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profile")
public class Profile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DietaryPreferenceType dietaryPreference = DietaryPreferenceType.NONE;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Allergy> allergies = new ArrayList<>();

    @OneToOne(mappedBy = "profile")
    @JoinColumn(name = "user_id")
    private RegularUser user;

    public Profile() {
    }

    public Profile(DietaryPreferenceType dietaryPreference,
            List<Allergy> allergies, RegularUser user) {
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
        this.allergies = new ArrayList<>(allergies);
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
    }
}
