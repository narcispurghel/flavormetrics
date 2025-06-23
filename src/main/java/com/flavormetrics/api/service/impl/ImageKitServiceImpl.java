package com.flavormetrics.api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flavormetrics.api.exception.ImageKitUploadException;
import com.flavormetrics.api.service.ImageKitService;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import jakarta.annotation.PostConstruct;

@Service
public class ImageKitServiceImpl implements ImageKitService {
    private static final ImageKit IMAGE_KIT = ImageKit.getInstance();

    @Value("${imagekit.url}") String publicKey;

    @Value("${imagekit.private-key}") String privateKey;

    @Value("${imagekit.public-key}") String urlEndpoint;

    @Override
    public String upload(String url, String fileName) {
        if (!fileName.contains(".")) {
            fileName = fileName + ".jpg";
        }
        FileCreateRequest fileCreateRequest = new FileCreateRequest(url, fileName);
        fileCreateRequest.setFolder("/flavormetrics");
        try {
            Result result = IMAGE_KIT.upload(fileCreateRequest);
            return result.getUrl();
        } catch (Exception e) {
            throw new ImageKitUploadException(e.getMessage(), e);
        }
    }

    @PostConstruct void configure() {
        Configuration config = new Configuration(publicKey, privateKey, urlEndpoint);
        IMAGE_KIT.setConfig(config);
    }
}