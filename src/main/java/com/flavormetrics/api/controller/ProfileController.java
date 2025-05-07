package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.ProfileDto;
import com.flavormetrics.api.model.request.CreateProfileRequest;
import com.flavormetrics.api.service.ProfileService;
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

    @GetMapping
    public ResponseEntity<Data<ProfileDto>> getProfile(Authentication authentication) {
        return ResponseEntity.ok(profileService.getProfile(authentication));
    }

    @PostMapping
    public ResponseEntity<Data<ProfileDto>> addProfile(
            @RequestBody Data<CreateProfileRequest>  request,
            Authentication authentication) {
        return ResponseEntity.ok(profileService.createProfile(request.data(), authentication));
    }
}