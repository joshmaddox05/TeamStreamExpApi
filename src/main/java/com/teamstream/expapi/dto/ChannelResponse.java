package com.teamstream.expapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Response DTO for channel creation
 */
public class ChannelResponse {
    
    private String channelName;
    private String token;
    private String appId;
    private Integer uid;
    private String role;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public ChannelResponse() {
        this.createdAt = LocalDateTime.now();
    }
    
    public ChannelResponse(String channelName, String token, String appId, Integer uid, 
                          String role, LocalDateTime expirationTime) {
        this.channelName = channelName;
        this.token = token;
        this.appId = appId;
        this.uid = uid;
        this.role = role;
        this.expirationTime = expirationTime;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getChannelName() {
        return channelName;
    }
    
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
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
    
    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }
    
    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
