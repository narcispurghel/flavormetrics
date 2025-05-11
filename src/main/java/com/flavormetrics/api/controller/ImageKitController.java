package com.flavormetrics.api.controller;

import com.flavormetrics.api.exception.impl.RecipeNotFoundException;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.UploadImage;
import com.flavormetrics.api.service.ImageKitService;
import com.flavormetrics.api.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recipe-image")
public class ImageKitController {
    private final ImageKitService imageKitService;
    private final RecipeService recipeService;

    public ImageKitController(ImageKitService imageKitService,
                              RecipeService recipeService) {
        this.imageKitService = imageKitService;
        this.recipeService = recipeService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Data<RecipeDto>> upload(
            @RequestBody Data<UploadImage> request,
            @PathVariable UUID id) {
        boolean recipeExists = recipeService.existsById(id);
        if (recipeExists) {
            String url = imageKitService.upload(request.data().url(), request.data().name());
            Data<RecipeDto> responseBody = recipeService.updateImageById(id, url);
            return ResponseEntity.ok(responseBody);
        }
        throw new RecipeNotFoundException(
                "Invalid recipe id",
                "Cannot find a recipe associated with id " + id,
                HttpStatus.BAD_REQUEST,
                "id");
    }
}