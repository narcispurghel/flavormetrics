package com.flavormetrics.api.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.flavormetrics.api.entity.User;
import com.flavormetrics.api.exception.impl.DuplicateEmailException;
import com.flavormetrics.api.exception.impl.NotAllowedRequestException;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.UserDto;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.UserService;
import com.flavormetrics.api.util.ModelMapper;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Data<UserDto> registerUser(RegisterRequest data, Authentication authentication) {

        boolean isEmailUsed = userRepository.existsByEmail(data.email());

        if (isEmailUsed) {
            throw new DuplicateEmailException(
                    "Invalid email",
                    "This email address is not available",
                    HttpStatus.CONFLICT,
                    "data.email");
        }

        boolean isAuthenticated = authentication.isAuthenticated();

        if (isAuthenticated) {
            throw new NotAllowedRequestException(
                    "Invalid request",
                    "Cannot perform register action because user is already authenticated",
                    HttpStatus.BAD_REQUEST,
                    "");
        }

        User user = userRepository.save(new User(
                data.email(),
                data.firstName(),
                data.lastName()));

        return Data.body(ModelMapper.toUserDto(user));
    }

}