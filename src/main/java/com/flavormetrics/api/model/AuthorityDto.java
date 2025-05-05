package com.flavormetrics.api.model;

import com.flavormetrics.api.model.enums.RoleType;

import java.util.UUID;

public record AuthorityDto(
        UUID id,
        RoleType role,
        UserDto user
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private RoleType role;
        private UserDto user;

        private Builder() {
            // Prevent instantiation
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder role(RoleType role) {
            this.role = role;
            return this;
        }

        public Builder user(UserDto user) {
            this.user = user;
            return this;
        }

        public AuthorityDto build() {
            return new AuthorityDto(id, role, user);
        }
    }
}
