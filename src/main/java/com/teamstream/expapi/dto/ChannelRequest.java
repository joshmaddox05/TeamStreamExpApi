package com.teamstream.expapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * Enhanced request DTO for channel creation with team management features
 */
public class ChannelRequest {
    
    // Core Requirements
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Event type is required")
    private String eventType;
    
    @NotNull(message = "UID is required")
    @Positive(message = "UID must be positive")
    private Integer uid;
    
    private String role = "publisher"; // Default role
    
    // Team Management Fields
    private String teamName;
    private String league;
    private String season;
    private String homeTeam;
    private String awayTeam;
    private String venue;
    private LocalDateTime gameTime;
    
    // Stream Configuration
    private String streamQuality = "720p"; // Default quality
    private boolean enableRecording = false;
    private boolean enableChat = true;
    private int maxViewers = 1000;
    
    // Optional metadata
    private String description;
    private String[] tags;
    
    public ChannelRequest() {}
    
    public ChannelRequest(String userId, String eventType, Integer uid, String role) {
        this.userId = userId;
        this.eventType = eventType;
        this.uid = uid;
        this.role = role;
    }
    
    // Core Getters and Setters
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
    
    // Team Management Getters and Setters
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public String getLeague() {
        return league;
    }
    
    public void setLeague(String league) {
        this.league = league;
    }
    
    public String getSeason() {
        return season;
    }
    
    public void setSeason(String season) {
        this.season = season;
    }
    
    public String getHomeTeam() {
        return homeTeam;
    }
    
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }
    
    public String getAwayTeam() {
        return awayTeam;
    }
    
    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }
    
    public String getVenue() {
        return venue;
    }
    
    public void setVenue(String venue) {
        this.venue = venue;
    }
    
    public LocalDateTime getGameTime() {
        return gameTime;
    }
    
    public void setGameTime(LocalDateTime gameTime) {
        this.gameTime = gameTime;
    }
    
    // Stream Configuration Getters and Setters
    public String getStreamQuality() {
        return streamQuality;
    }
    
    public void setStreamQuality(String streamQuality) {
        this.streamQuality = streamQuality;
    }
    
    public boolean isEnableRecording() {
        return enableRecording;
    }
    
    public void setEnableRecording(boolean enableRecording) {
        this.enableRecording = enableRecording;
    }
    
    public boolean isEnableChat() {
        return enableChat;
    }
    
    public void setEnableChat(boolean enableChat) {
        this.enableChat = enableChat;
    }
    
    public int getMaxViewers() {
        return maxViewers;
    }
    
    public void setMaxViewers(int maxViewers) {
        this.maxViewers = maxViewers;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String[] getTags() {
        return tags;
    }
    
    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
