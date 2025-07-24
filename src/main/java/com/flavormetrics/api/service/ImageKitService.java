package com.flavormetrics.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageKitService {
  String upload(String url, String fileName);

  String upload(MultipartFile file, String fileName);
}
