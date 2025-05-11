package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.exception.impl.ImageKitUploadException;
import com.flavormetrics.api.service.ImageKitService;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
            throw new ImageKitUploadException(
                    "Cannot upload the file",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "upload"
            );
        }
    }

    @PostConstruct
    private void configure() {
        Configuration config = new Configuration(publicKey, privateKey, urlEndpoint);
        IMAGE_KIT.setConfig(config);
    }
}