package com.flavormetrics.api.entity.user.impl;

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

    private RegularUser(Builder builder) {
        this.profile = builder.profile;
    }

    public static class Builder {

        private Profile profile;

        public Builder() {
            // TODO ad argument
        }

        public Builder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public RegularUser build() {
            return new RegularUser(this);
        }
    }

    public Profile getProfile() {
        return profile;
    }
}
