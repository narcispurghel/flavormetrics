package com.flavormetrics.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.user.UserDto;
import com.flavormetrics.api.model.user.impl.NutritionistDto;
import com.flavormetrics.api.model.user.impl.RegularUserDto;
import com.flavormetrics.api.repository.NutritionistRepository;
import com.flavormetrics.api.repository.RegularUserRepository;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.UserService;
import com.flavormetrics.api.util.ModelConverter;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final NutritionistRepository nutritionistRepository;
    private final RegularUserRepository regularUserRepository;

    public UserServiceImpl(UserRepository userRepository,
                           NutritionistRepository nutritionistRepository,
                           RegularUserRepository regularUserRepository) {
        this.userRepository = userRepository;
        this.nutritionistRepository = nutritionistRepository;
        this.regularUserRepository = regularUserRepository;
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
        List<RegularUserDto> regularUsers = regularUserRepository.findAll()
                .stream()
                .map(ModelConverter::toRegularUserDto)
                .toList();
        return Data.body(regularUsers);
    }

    @Override
    public Data<List<NutritionistDto>> getAllNutritionistUsers() {
        List<NutritionistDto> nutritionists = nutritionistRepository.findAll()
                .stream()
                .map(ModelConverter::toNutritionistDto)
                .toList();
        return Data.body(nutritionists);
    }
}