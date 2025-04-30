package com.flavormetrics.api.entity.user.impl;

import com.flavormetrics.api.entity.Profile;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.model.enums.RoleType;
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
        super(builder);
        this.profile = builder.profile;
    }

    public static class Builder extends User.Builder<Builder> {
        private Profile profile;

        public Builder(String password, String username, RoleType role) {
            super(password, username, role);
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
