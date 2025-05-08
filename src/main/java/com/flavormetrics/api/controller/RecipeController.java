package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.*;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.response.ApiErrorResponse;
import com.flavormetrics.api.model.response.RecipesByNutritionistResponse;
import com.flavormetrics.api.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Create e new recipe", description = "Requires to be authenticated as nutritionist user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
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
    @PostMapping("/protected/add")
    public ResponseEntity<Data<RecipeDto>> addRecipe(
            @RequestBody Data<AddRecipeRequest> request,
            Authentication authentication) {
        Data<RecipeDto> responseBody = recipeService.add(request.data(), authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseBody);
    }

    @Operation(summary = "Update a recipe by id", description = "Requires to be authenticated as nutritionist user and to be owner of the recipe")
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
    @PutMapping("/protected/update/{id}")
    public ResponseEntity<Data<RecipeDto>> updateRecipeById(
            @PathVariable UUID id,
            @RequestBody Data<AddRecipeRequest> request,
            Authentication authentication) {
        Data<RecipeDto> responseBody = recipeService.updateById(id, request.data(), authentication);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseBody);
    }

    @Operation(summary = "Delete a recipe by id", description = "Requires to be authenticated as nutritionist user and to be owner of the recipe")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operation success",
                    content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json")
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
    @DeleteMapping("/protected/delete/{id}")
    public ResponseEntity<Data<String>> deleteById(
            @PathVariable UUID id) {
        Data<String> responseBody = recipeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(responseBody);
    }

    @Operation(summary = "Get all recipes", description = "Can be accessed without authentication")
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
    @GetMapping("/all")
    public ResponseEntity<Data<List<RecipeDto>>> getALL() {
        return ResponseEntity.ok(recipeService.getAll());
    }

    @Operation(summary = "Get all recipes", description = "Can be accessed without authentication")
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
    @GetMapping("/byId/{id}")
    public ResponseEntity<Data<RecipeDto>> getById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @Operation(summary = "Get all recipes", description = "Can be accessed without authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RecipesByNutritionistResponse.class), mediaType = "application/json")
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
    @GetMapping("/byNutritionist/{username}")
    public ResponseEntity<Data<RecipesByNutritionistResponse>> getByNutritionist(
            @PathVariable String username) {
        return ResponseEntity.ok(recipeService.getByNutritionist(username));
    }

    @Operation(summary = "Get all recipes by current user's profile", description = "Requires to be authenticated as regular user")
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
    @GetMapping("/byProfile")
    public ResponseEntity<Data<List<RecipeDto>>> getAllByProfile(Authentication authentication) {
        ProfileFilter profileFilter = recipeService.getProfilePreferences(authentication.getName());
        return ResponseEntity.ok(recipeService.findAllByProfilePreferences(profileFilter));
    }

    @Operation(summary = "Get all recipes", description = "Can be accessed without authentication")
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
    @PostMapping("/byFilter")
    public ResponseEntity<Data<List<RecipeDto>>> getAllByFilter(@RequestBody RecipeDefaultFilter recipeDefaultFilter) {
        return ResponseEntity.ok(recipeService.findAllByDefaultFilter(recipeDefaultFilter));
    }
}