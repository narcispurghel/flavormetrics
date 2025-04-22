package com.flavormetrics.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("hello from flavormetrics", HttpStatus.OK);
    }
}
