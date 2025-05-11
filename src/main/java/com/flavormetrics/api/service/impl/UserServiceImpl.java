package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.user.UserDto;
import com.flavormetrics.api.model.user.impl.NutritionistDto;
import com.flavormetrics.api.model.user.impl.RegularUserDto;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.UserService;
import com.flavormetrics.api.util.ModelConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Data<List<UserDto>> getAllUsers() {
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(ModelConverter::toUserDto)
                .toList();

        return Data.body(users);
    }

    @Override
    public Data<List<RegularUserDto>> getAllRegularUsers() {

        return null;
    }

    @Override
    public Data<List<NutritionistDto>> getAllNutritionistUsers() {
        return null;
    }
}