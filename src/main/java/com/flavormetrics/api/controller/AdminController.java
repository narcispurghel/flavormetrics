package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.user.UserDto;
import com.flavormetrics.api.model.user.impl.NutritionistDto;
import com.flavormetrics.api.model.user.impl.RegularUserDto;
import com.flavormetrics.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<Data<List<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/regular")
    public ResponseEntity<Data<List<RegularUserDto>>> getAllRegularUsers() {
        return ResponseEntity.ok(userService.getAllRegularUsers());
    }

    @GetMapping("/nutritionist")
    public ResponseEntity<Data<List<NutritionistDto>>> getAllNutritionistUsers() {
        return ResponseEntity.ok(userService.getAllNutritionistUsers());
    }
}