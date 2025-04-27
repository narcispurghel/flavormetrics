package com.flavormetrics.api.service;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.UserDto;
import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.model.response.LoginResponse;
import org.springframework.security.core.Authentication;

public interface UserService {

    Data<UserDto> registerUser(RegisterRequest data, Authentication authentication);

    LoginResponse authenticate(LoginRequest data, Authentication authentication);

    boolean existsByEmail(String email, RoleType role);
}
