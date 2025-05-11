package com.flavormetrics.api.service;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.user.UserDto;
import com.flavormetrics.api.model.user.impl.NutritionistDto;
import com.flavormetrics.api.model.user.impl.RegularUserDto;

import java.util.List;

public interface UserService {
    Data<List<UserDto>> getAllUsers();

    Data<List<RegularUserDto>> getAllRegularUsers();

    Data<List<NutritionistDto>> getAllNutritionistUsers();
}
