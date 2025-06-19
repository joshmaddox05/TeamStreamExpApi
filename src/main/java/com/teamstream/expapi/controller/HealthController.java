package com.teamstream.expapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health and info controller for the API
 */
@RestController
@RequestMapping("/")
public class HealthController {

    /**
     * Root health check endpoint
     * GET /api/v1/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "TeamStream Experience API");
        response.put("version", "1.0.0");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * API documentation endpoint
     * GET /api/v1/
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> apiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "TeamStream Experience API");
        response.put("version", "1.0.0");
        response.put("description", "Java Spring Boot API for live streaming with Agora SDK integration");
        response.put("documentation", Map.of(
            "endpoints", Map.of(
                "GET /api/v1/", "API information",
                "GET /api/v1/health", "Health check",
                "POST /api/v1/channel/create", "Create channel and generate token",
                "GET /api/v1/channel/health", "Channel service health",
                "GET /api/v1/channel/info", "Channel service info"
            ),
            "authentication", "API Key required in X-API-Key header for protected endpoints"
        ));
        
        return ResponseEntity.ok(response);
    }
}
