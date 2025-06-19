package com.teamstream.expapi.controller;

import com.teamstream.expapi.dto.ChannelRequest;
import com.teamstream.expapi.dto.ChannelResponse;
import com.teamstream.expapi.dto.ErrorResponse;
import com.teamstream.expapi.dto.TokenRefreshRequest;
import com.teamstream.expapi.service.AgoraChannelService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Agora channel management
 */
@RestController
@RequestMapping("/channel")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChannelController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    private final AgoraChannelService agoraChannelService;

    @Autowired
    public ChannelController(AgoraChannelService agoraChannelService) {
        this.agoraChannelService = agoraChannelService;
    }

    /**
     * Creates a new channel and generates an Agora RTC token
     * POST /api/v1/channel/create
     */
    @PostMapping("/create")
    public ResponseEntity<?> createChannel(@Valid @RequestBody ChannelRequest request, 
                                         BindingResult bindingResult) {
        
        logger.info("Received channel creation request for user: {}", request.getUserId());

        // Validate request
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Validation Error", 
                                      "Invalid request parameters: " + errors, 
                                      HttpStatus.BAD_REQUEST.value()));
        }

        // Check if Agora configuration is valid
        if (!agoraChannelService.isConfigurationValid()) {
            logger.error("Agora configuration is invalid");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Configuration Error", 
                                      "Agora SDK is not properly configured", 
                                      HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        try {
            ChannelResponse response = agoraChannelService.createChannel(request);
            logger.info("Successfully created channel: {} for user: {}", 
                       response.getChannelName(), request.getUserId());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to create channel for user: {}", request.getUserId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Channel Creation Error", 
                                      e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    /**
     * Health check endpoint for the channel service
     * GET /api/v1/channel/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Agora Channel Service");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        health.put("configurationValid", agoraChannelService.isConfigurationValid());
        
        return ResponseEntity.ok(health);
    }

    /**
     * Get service information
     * GET /api/v1/channel/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getServiceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "TeamStream Experience API");
        info.put("version", "1.0.0");
        info.put("description", "Agora SDK integration for live streaming with team management");
        info.put("features", Map.of(
            "token_generation", "Secure Agora RTC token generation",
            "team_management", "Basketball team and game information",
            "stream_configuration", "Camera switching, recording, and quality settings",
            "token_refresh", "Token renewal for long-running streams"
        ));
        info.put("endpoints", Map.of(
            "POST /channel/create", "Create a new channel and generate token",
            "POST /channel/refresh-token", "Refresh an existing token",
            "GET /channel/health", "Service health check",
            "GET /channel/info", "Service information"
        ));
        
        return ResponseEntity.ok(info);
    }

    /**
     * Refresh token for an existing channel
     * POST /api/v1/channel/refresh-token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request, 
                                        BindingResult bindingResult) {
        
        logger.info("Received token refresh request for channel: {}", request.getChannelName());

        // Validate request
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Validation Error", 
                                      "Invalid request parameters: " + errors, 
                                      HttpStatus.BAD_REQUEST.value()));
        }

        try {
            ChannelResponse response = agoraChannelService.refreshToken(request);
            logger.info("Successfully refreshed token for channel: {}", request.getChannelName());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to refresh token for channel: {}", request.getChannelName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Token Refresh Error", 
                                      e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    /**
     * Global exception handler for this controller
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        logger.error("Unhandled exception in ChannelController", e);
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Internal Server Error",
            "An unexpected error occurred: " + e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
