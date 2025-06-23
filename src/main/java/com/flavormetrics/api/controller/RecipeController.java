package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.DataWithPagination;
import com.flavormetrics.api.model.RecipeByOwner;
import com.flavormetrics.api.model.RecipeDto;
import com.flavormetrics.api.model.RecipeFilter;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.model.response.ApiErrorResponse;
import com.flavormetrics.api.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Create e new recipe", description = "Requires to be authenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class),
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(
            @RequestBody AddRecipeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Recipe create successfully", "id",
                        String.valueOf(recipeService.create(request))));
    }


    @Operation(summary = "Get a recipe by given id", description = "Can be accessed without authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class),
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getById(
            @PathVariable("id") UUID id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @Operation(summary = "Update a recipe by id", description = "Requires to be authenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class),
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found", content
                    = @Content(mediaType = "application/json", schema = @Schema(implementation =
                    ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<RecipeDto> updateRecipeById(
            @PathVariable UUID id,
            @RequestBody AddRecipeRequest request) {
        RecipeDto responseBody = recipeService.updateById(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @Operation(summary = "Delete a recipe by id", description = "Requires to be authenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success",
                    content = @Content(schema = @Schema(implementation = String.class),
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Recipe not found", content
                    = @Content(mediaType = "application/json", schema = @Schema(implementation =
                    ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        recipeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get all recipes", description = "Can be accessed without authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class),
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<DataWithPagination<List<RecipeDto>>> getALL(
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok(recipeService.findAll(pageNumber, pageSize));
    }


    @Operation(summary = "Get all recipes by user's email",
            description = "Can be accessed without authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class),
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = " application / json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })

    @GetMapping("/byOwner/{email}")
    public ResponseEntity<DataWithPagination<RecipeByOwner>> getAllByUserEmail(
            @PathVariable("email") String email,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok(recipeService.findAllByUserEmail(email, pageNumber, pageSize));
    }


    @Operation(summary = "Get all recipes by specified filter", description =
            "Can be accessed without authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success", content
                    = @Content(schema = @Schema(implementation = RecipeDto.class), mediaType =
                    "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/byFilter")
    public ResponseEntity<DataWithPagination<List<RecipeDto>>> getAllByFilter(
            @RequestBody RecipeFilter filter,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok(recipeService.findAllByRecipeFilter(filter, pageNumber, pageSize));
    }

    @Operation(summary = "Get recommendations by profile",
            description = "Requires to be authenticated and a profile created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success", content
                    = @Content(schema = @Schema(implementation = DataWithPagination.class), mediaType =
                    "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation =
                    String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/recommendations")
    public ResponseEntity<DataWithPagination<Set<RecipeDto>>> getRecommendations(
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok(recipeService.getRecommendations(pageNumber, pageSize));
    }
}