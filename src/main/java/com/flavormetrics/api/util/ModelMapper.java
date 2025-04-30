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

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName());
    }
}
