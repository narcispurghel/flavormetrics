package com.flavormetrics.api.service;

import com.flavormetrics.api.model.UserDto;

import java.util.Set;

public interface UserService {

    Set<UserDto> getAllUsers();

}
