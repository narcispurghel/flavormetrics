package com.flavormetrics.api.entity.user.impl;

import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.entity.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "regular_user", schema = "users")
public class RegularUser extends User {
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public RegularUser() {
        // No args constructor for JPA
    }

    public RegularUser(String password, Email username) {
        super(password, username);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
