package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.mapper.UserMapper;
import com.flavormetrics.api.model.UserDto;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<UserDto> getAllUsers() {
        return userRepository.findAllComplete()
        .stream()
        .map(UserMapper::toUserDto)
        .collect(Collectors.toUnmodifiableSet());
    }

}