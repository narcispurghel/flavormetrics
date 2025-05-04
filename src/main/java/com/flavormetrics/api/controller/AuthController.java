package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.model.response.LoginResponse;
import com.flavormetrics.api.model.response.RegisterResponse;
import com.flavormetrics.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Data<RegisterResponse>> register(
            @RequestBody Data<RegisterRequest> requestBody,
            Authentication authentication) {
        return ResponseEntity.ok(authService.registerUser(requestBody.data(), authentication));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Data<LoginRequest> requestBody,
                                               Authentication authentication) {
        return ResponseEntity.ok(authService.authenticate(requestBody.data(), authentication));
    }

}
