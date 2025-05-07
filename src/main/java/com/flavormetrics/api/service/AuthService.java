package com.flavormetrics.api.service;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.model.response.LoginResponse;
import com.flavormetrics.api.model.response.RegisterResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Data<RegisterResponse> registerUser(RegisterRequest data, Authentication authentication);
    LoginResponse authenticate(LoginRequest data, Authentication authentication);
    boolean existsByEmail(String email);
}
