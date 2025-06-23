package com.flavormetrics.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavormetrics.api.service.ImageKitService;
import com.flavormetrics.api.service.RecipeService;

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

    // @Operation(
    //         summary = "Update a recipe's image url by given id",
    //         description = "Requires to be authenticated as nutritionist and to be the owner of the recipe"
    // )
    // @ApiResponses(score = {
    //         @ApiResponse(
    //                 responseCode = "200",
    //                 description = "Operation success",
    //                 content = @Content(schema = @Schema(implementation = RecipeDto.class), mediaType = "application/json")
    //         ),
    //         @ApiResponse(
    //                 responseCode = "400",
    //                 description = "Invalid request data",
    //                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
    //         ),
    //         @ApiResponse(
    //                 responseCode = "401",
    //                 description = "Unauthenticated",
    //                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    //         ),
    //         @ApiResponse(
    //                 responseCode = "500",
    //                 description = "Internal Server Error",
    //                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
    //         )
    // })
    // @PatchMapping("/{id}")
    // public ResponseEntity<Data<RecipeDto>> upload(
    //         @RequestBody Data<UploadImage> request,
    //         @PathVariable UUID id) {
    //     boolean recipeExists = recipeService.existsById(id);
    //     if (recipeExists) {
    //         final String url = imageKitService.upload(request.data().url(), request.data().name());
    //         final Data<RecipeDto> responseBody = recipeService.updateImageById(id, url);
    //         return ResponseEntity.ok(responseBody);
    //     }
    //     throw new RecipeNotFoundException(
    //             "Invalid recipe id",
    //             "Cannot find a recipe associated with id " + id,
    //             HttpStatus.BAD_REQUEST,
    //             "id"
    //     );
    // }
}