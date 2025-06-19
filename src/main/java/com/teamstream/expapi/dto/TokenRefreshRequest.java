package com.teamstream.expapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for token refresh operations
 */
public class TokenRefreshRequest {
    
    @NotBlank(message = "Channel name is required")
    private String channelName;
    
    @NotNull(message = "UID is required")
    private Integer uid;
    
    private String role = "publisher"; // Default role
    
    public TokenRefreshRequest() {}
    
    public TokenRefreshRequest(String channelName, Integer uid, String role) {
        this.channelName = channelName;
        this.uid = uid;
        this.role = role;
    }
    
    // Getters and Setters
    public String getChannelName() {
        return channelName;
    }
    
    public void setChannelName(String channelName) {
        this.channelName = channelName;
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
