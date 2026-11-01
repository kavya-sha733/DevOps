package com.selfhealing.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @Value("${APP_NAME:self-healing-app}")
    private String appName;

    @Value("${APP_VERSION:1.0.0}")
    private String appVersion;

    @Value("${APP_BREAK:false}")
    private boolean appBreak;

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(Map.of(
            "app", appName,
            "version", appVersion
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        if (appBreak) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                        "status", "DOWN",
                        "message", "Simulated failure via APP_BREAK=true"
                    ));
        }
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "Application is healthy"
        ));
    }
}

