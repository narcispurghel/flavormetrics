package com.flavormetrics.api.controller;

import com.flavormetrics.api.exception.impl.RecipeNotFoundException;
import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.UploadImage;
import com.flavormetrics.api.model.response.ApiErrorResponse;
import com.flavormetrics.api.service.ImageKitService;
import com.flavormetrics.api.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Update a recipe's image url by given id",
            description = "Requires to be authenticated as nutritionist and to be the owner of the recipe"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class), mediaType = "application/json")
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
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Data<RecipeDto>> upload(
            @RequestBody Data<UploadImage> request,
            @PathVariable UUID id) {
        boolean recipeExists = recipeService.existsById(id);
        if (recipeExists) {
            final String url = imageKitService.upload(request.data().url(), request.data().name());
            final Data<RecipeDto> responseBody = recipeService.updateImageById(id, url);
            return ResponseEntity.ok(responseBody);
        }
        throw new RecipeNotFoundException(
                "Invalid recipe id",
                "Cannot find a recipe associated with id " + id,
                HttpStatus.BAD_REQUEST,
                "id");
    }
}