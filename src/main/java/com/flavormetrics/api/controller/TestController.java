package com.flavormetrics.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String index() {
        return "FlavorMetrics API";
    }

    @GetMapping("/test")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("hello from flavormetrics", HttpStatus.OK);
    }

    @GetMapping("/api/test/protected")
    public String testProtectedEndpoint() {
        return "hello from protected";
    }
}
