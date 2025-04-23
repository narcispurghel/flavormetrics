package com.flavormetrics.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.UserDto;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("path")
    public ResponseEntity<Data<UserDto>> register(@RequestBody Data<RegisterRequest> requestBody) {
        return ResponseEntity.ok(userService.registerUser(requestBody.data()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest data) {
        //TODO: process POST /login request
        
        return null;
    }
    
    
}
