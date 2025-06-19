package com.teamstream.expapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for channel creation
 */
public class ChannelRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Event type is required")
    private String eventType;
    
    @NotNull(message = "UID is required")
    @Positive(message = "UID must be positive")
    private Integer uid;
    
    private String role = "publisher"; // Default role
    
    public ChannelRequest() {}
    
    public ChannelRequest(String userId, String eventType, Integer uid, String role) {
        this.userId = userId;
        this.eventType = eventType;
        this.uid = uid;
        this.role = role;
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public Integer getUid() {
        return uid;
    }
    
    public void setUid(Integer uid) {
        this.uid = uid;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
