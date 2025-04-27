package com.flavormetrics.api.util;

import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.model.UserDto;

public class ModelMapper {

    private ModelMapper() {

    }

    public static UserDto toUserDto(User user) {

        if (user == null) {
            return null;
        }

        //TODO actualize UserDto
        //UUID profileId = user.getProfile() == null ? null : user.getProfile().getId();

        return new UserDto(
                user.getUserDetails().getId(),
                user.getUserDetails().getUsername(),
                user.getFirstName(),
                user.getLastName(),
                null);
    }
}
