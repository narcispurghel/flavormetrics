package com.flavormetrics.api.util;

import java.util.UUID;

import com.flavormetrics.api.entity.User;
import com.flavormetrics.api.model.UserDto;

public class ModelMapper {

    private ModelMapper() {

    }

    public static UserDto toUserDto(User user) {

        if (user == null) {
            return null;
        }

        UUID profileId = user.getProfile() == null ? null : user.getProfile().getId();

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                profileId);
    }
}
