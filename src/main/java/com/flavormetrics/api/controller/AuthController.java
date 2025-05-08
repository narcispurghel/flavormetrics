package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.model.response.ApiErrorResponse;
import com.flavormetrics.api.model.response.LoginResponse;
import com.flavormetrics.api.model.response.RegisterResponse;
import com.flavormetrics.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Register a user", description = "Register a user based on request data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User account created",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username is not available",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<Data<RegisterResponse>> register(
            @RequestBody Data<RegisterRequest> requestBody,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerUser(requestBody.data(), authentication));
    }

    @Operation(summary = "Login a user", description = "Login in a user based on request data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login success",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bad credentials",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Data<LoginRequest> requestBody,
                                               Authentication authentication) {
        return ResponseEntity.ok(authService.authenticate(requestBody.data(), authentication));
    }

}
