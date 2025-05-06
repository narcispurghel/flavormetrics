package com.flavormetrics.api.service;

import com.flavormetrics.api.model.Data;

import java.nio.file.Path;

public interface ImageKitService {
    Data<String> upload(String url, String fileName);

    Data<String> upload(byte[] fileData, String fileName);

    Data<String> upload(Path path, String fileName);
}
