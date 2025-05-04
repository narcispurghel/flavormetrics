package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.response.AddRecipeResponse;
import com.flavormetrics.api.model.response.RecipesByNutritionistResponse;
import com.flavormetrics.api.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/protected/add")
    public ResponseEntity<Data<AddRecipeResponse>> addRecipe(
            @RequestBody Data<AddRecipeRequest> request,
            Authentication authentication) {
        Data<AddRecipeResponse> responseBody = recipeService.add(request.data(), authentication);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseBody);
    }

    @PutMapping("/protected/update/{id}")
    public ResponseEntity<Data<AddRecipeResponse>> updateRecipeById(
            @PathVariable UUID id,
            @RequestBody Data<AddRecipeRequest> request,
            Authentication authentication) {
        Data<AddRecipeResponse> responseBody = recipeService.updateById(id, request.data(), authentication);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseBody);
    }

    @DeleteMapping("/protected/delte/{id}")
    public ResponseEntity<Data<String>> deleteById(
            @PathVariable UUID id) {
        Data<String> responseBody = recipeService.deleteById(id);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/all")
    public ResponseEntity<Data<List<RecipeDto>>> getALL() {
        return ResponseEntity.ok(recipeService.getAll());
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<Data<AddRecipeResponse>> getById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @GetMapping("/byNutritionist/{username}")
    public ResponseEntity<Data<RecipesByNutritionistResponse>> getByNutritionist(
            @PathVariable String username) {
        return ResponseEntity.ok(recipeService.getByNutritionist(username));
    }
}