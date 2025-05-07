package com.flavormetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record RegularUserDto(
        @JsonIgnore
        UUID id,
        String firstName,
        String lastName,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled,
        String password,
        String username,
        LocalDateTime updatedAt,
        LocalDateTime createdAt,
        List<AuthorityDto> authorities,
        ProfileDto profile
) implements UserDto {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String firstName;
        private String lastName;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;
        private String password;
        private String username;
        private LocalDateTime updatedAt;
        private LocalDateTime createdAt;
        private List<AuthorityDto> authorities;
        private ProfileDto profileDto;

        private Builder() {
            // Prevent instantiation
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public Builder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder authorities(List<AuthorityDto> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public Builder profileDto(ProfileDto profileDto) {
            this.profileDto = profileDto;
            return this;
        }

        public RegularUserDto build() {
            return new RegularUserDto(
                    id, firstName, lastName, accountNonExpired, accountNonLocked,
                    credentialsNonExpired, enabled, password, username,
                    updatedAt, createdAt, authorities, profileDto
            );
        }
    }
}
