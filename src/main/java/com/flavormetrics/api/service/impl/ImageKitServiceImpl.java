package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.service.ImageKitService;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageKitServiceImpl implements ImageKitService {
    private static final ImageKit IMAGE_KIT = ImageKit.getInstance();

    @Value("${imagekit.url.endpoint}")
    private String publicKey;

    @Value("${imagekit.private.key}")
    private String privateKey;

    @Value("${imagekit.public.key}")
    private String urlEndpoint;

    @Override
    @Transactional
    public Data<String> upload(String url, String fileName) {
        if (!fileName.contains(".")) {
            fileName = fileName + ".jpg";
        }
        FileCreateRequest fileCreateRequest = new FileCreateRequest(url, fileName);
        fileCreateRequest.setFolder("/flavormetrics");
        try {
            Result result = IMAGE_KIT.upload(fileCreateRequest);
            return Data.body(result.getUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Data<String> upload(byte[] fileData, String fileName) {
        if (!fileName.contains(".")) {
            fileName = fileName + ".jpg";
        }
        FileCreateRequest fileCreateRequest = new FileCreateRequest(fileData, fileName);
        fileCreateRequest.setFolder("/flavormetrics");
        try {
            Result result = IMAGE_KIT.upload(fileCreateRequest);
            return Data.body(result.getUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Data<String> upload(Path path, String fileName) {
        try {
            byte[] imageData = Files.readAllBytes(path);
            return upload(imageData, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void configure() {
        Configuration config = new Configuration(publicKey, privateKey, urlEndpoint);
        IMAGE_KIT.setConfig(config);
    }
}