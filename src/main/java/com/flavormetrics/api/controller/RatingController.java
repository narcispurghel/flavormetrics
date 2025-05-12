package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.RatingDto;
import com.flavormetrics.api.model.response.ApiErrorResponse;
import com.flavormetrics.api.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    @Operation(summary = "Create e new rating", description = "Requires to be authenticated")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
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
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping("/{recipeId}")
    public ResponseEntity<Data<String>> addRating(
            @PathVariable UUID recipeId,
            @RequestBody Data<Integer> requestBody,
            Authentication authentication) {
        return ResponseEntity.ok(ratingService.addRecipeRating(recipeId, requestBody.data(), authentication));
    }

    @Operation(summary = "Get all recipe's ratings by id", description = "Requires to be authenticated")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RatingDto.class), mediaType = "application/json")
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
    @GetMapping("/{recipeId}/all")
    public ResponseEntity<Data<List<RatingDto>>> getAllRatingsByRecipeId(@PathVariable UUID recipeId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByRecipeId(recipeId));
    }

    @Operation(summary = "Get all user's ratings", description = "Requires to be authenticated")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Operation success",
                    content = @Content(schema = @Schema(implementation = RatingDto.class), mediaType = "application/json")
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
    @GetMapping("/byUser")
    public ResponseEntity<Data<List<RatingDto>>> getAllRatingsByUser(Authentication authentication) {
        return ResponseEntity.ok(ratingService.getAllRatingsByUser(authentication));
    }
}