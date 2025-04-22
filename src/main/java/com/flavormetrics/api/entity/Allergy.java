package com.flavormetrics.api.entity;

import com.flavormetrics.api.enums.AllergyType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "allergy", schema = "profile")
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private AllergyType type;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public Allergy() {}

    public Allergy(AllergyType type) {
        if (type == null) {
            throw new IllegalArgumentException("Allergy type cannot be null");
        }
        this.type = type;
        this.description = type.getDescription();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AllergyType getType() {
        return type;
    }

    public void setType(AllergyType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
