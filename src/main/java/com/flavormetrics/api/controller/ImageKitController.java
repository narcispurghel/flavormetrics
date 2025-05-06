package com.flavormetrics.api.controller;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.UploadImage;
import com.flavormetrics.api.service.ImageKitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recipe-image")
public class ImageKitController {
    private final ImageKitService imageKitService;

    public ImageKitController(ImageKitService imageKitService) {
        this.imageKitService = imageKitService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Data<String>> upload(
            @RequestBody Data<UploadImage> request,
            @PathVariable UUID id) {
        return ResponseEntity.ok(imageKitService.upload(request.data().url(), request.data().name()));
    }
}