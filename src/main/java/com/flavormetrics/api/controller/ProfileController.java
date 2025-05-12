package com.flavormetrics.api.controller;

import com.flavormetrics.api.exception.ApiException;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.ProfileDto;
import com.flavormetrics.api.model.request.CreateProfileRequest;
import com.flavormetrics.api.model.response.ApiErrorResponse;
import com.flavormetrics.api.model.response.LoginResponse;
import com.flavormetrics.api.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Get user profile", description = "Get associated user profile from authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(schema = @Schema(implementation = ProfileDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Operation success but user doesn't have an associated profile yet",
                    content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<Data<ProfileDto>> getProfile(Authentication authentication) {
        Data<ProfileDto> response = profileService.getProfile(authentication);
        if (response.data() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add user profile", description = "Associate a profile to current user from authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(schema = @Schema(implementation = ProfileDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Data<ProfileDto>> addProfile(
            @RequestBody Data<CreateProfileRequest> request,
            Authentication authentication) {
        return ResponseEntity.ok(profileService.createProfile(request.data(), authentication));
    }
}