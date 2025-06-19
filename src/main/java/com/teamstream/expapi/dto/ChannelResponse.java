package com.teamstream.expapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Enhanced response DTO for channel creation with team management features
 */
public class ChannelResponse {
    
    // Core Agora Information
    private String channelName;
    private String token;
    private String appId;
    private Integer uid;
    private String role;
    
    // Team Management Information
    private String userId;
    private String eventType;
    private String streamType;
    private String streamQuality;
    
    // Stream Configuration
    private StreamConfig streamConfig;
    private TeamInfo teamInfo;
    private Map<String, Object> metadata;
    
    // Timestamps
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Token Management
    private Integer tokenExpirationSeconds;
    private String refreshEndpoint;
    
    public ChannelResponse() {
        this.createdAt = LocalDateTime.now();
        this.metadata = new HashMap<>();
        this.streamConfig = new StreamConfig();
        this.teamInfo = new TeamInfo();
    }
    
    public ChannelResponse(String channelName, String token, String appId, Integer uid, 
                          String role, LocalDateTime expirationTime, String userId, String eventType) {
        this.channelName = channelName;
        this.token = token;
        this.appId = appId;
        this.uid = uid;
        this.role = role;
        this.expirationTime = expirationTime;
        this.userId = userId;
        this.eventType = eventType;
        this.createdAt = LocalDateTime.now();
        this.metadata = new HashMap<>();
        this.streamConfig = new StreamConfig();
        this.teamInfo = new TeamInfo();
        this.refreshEndpoint = "/api/v1/channel/refresh-token";
        
        // Calculate token expiration in seconds
        if (expirationTime != null) {
            this.tokenExpirationSeconds = (int) java.time.Duration.between(
                LocalDateTime.now(), expirationTime).getSeconds();
        }
    }
    
    // Core Agora Information Getters/Setters
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
    
    // Team Management Getters/Setters
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
    
    public String getStreamType() {
        return streamType;
    }
    
    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }
    
    public String getStreamQuality() {
        return streamQuality;
    }
    
    public void setStreamQuality(String streamQuality) {
        this.streamQuality = streamQuality;
    }
    
    // Stream Configuration Getters/Setters
    public StreamConfig getStreamConfig() {
        return streamConfig;
    }
    
    public void setStreamConfig(StreamConfig streamConfig) {
        this.streamConfig = streamConfig;
    }
    
    public TeamInfo getTeamInfo() {
        return teamInfo;
    }
    
    public void setTeamInfo(TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    // Timestamp Getters/Setters
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
    
    // Token Management Getters/Setters
    public Integer getTokenExpirationSeconds() {
        return tokenExpirationSeconds;
    }
    
    public void setTokenExpirationSeconds(Integer tokenExpirationSeconds) {
        this.tokenExpirationSeconds = tokenExpirationSeconds;
    }
    
    public String getRefreshEndpoint() {
        return refreshEndpoint;
    }
    
    public void setRefreshEndpoint(String refreshEndpoint) {
        this.refreshEndpoint = refreshEndpoint;
    }
    
    // Utility methods
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }
    
    /**
     * Stream Configuration nested class
     */
    public static class StreamConfig {
        private boolean enableCameraSwitching = true;
        private boolean enableFullScreen = true;
        private boolean enableRecording = false;
        private String videoProfile = "720p";
        private String audioProfile = "high_quality";
        private int maxViewers = 1000;
        private boolean enableChat = true;
        private boolean enableLandscapeMode = true;
        
        // Getters and Setters
        public boolean isEnableCameraSwitching() {
            return enableCameraSwitching;
        }
        
        public void setEnableCameraSwitching(boolean enableCameraSwitching) {
            this.enableCameraSwitching = enableCameraSwitching;
        }
        
        public boolean isEnableFullScreen() {
            return enableFullScreen;
        }
        
        public void setEnableFullScreen(boolean enableFullScreen) {
            this.enableFullScreen = enableFullScreen;
        }
        
        public boolean isEnableRecording() {
            return enableRecording;
        }
        
        public void setEnableRecording(boolean enableRecording) {
            this.enableRecording = enableRecording;
        }
        
        public String getVideoProfile() {
            return videoProfile;
        }
        
        public void setVideoProfile(String videoProfile) {
            this.videoProfile = videoProfile;
        }
        
        public String getAudioProfile() {
            return audioProfile;
        }
        
        public void setAudioProfile(String audioProfile) {
            this.audioProfile = audioProfile;
        }
        
        public int getMaxViewers() {
            return maxViewers;
        }
        
        public void setMaxViewers(int maxViewers) {
            this.maxViewers = maxViewers;
        }
        
        public boolean isEnableChat() {
            return enableChat;
        }
        
        public void setEnableChat(boolean enableChat) {
            this.enableChat = enableChat;
        }
        
        public boolean isEnableLandscapeMode() {
            return enableLandscapeMode;
        }
        
        public void setEnableLandscapeMode(boolean enableLandscapeMode) {
            this.enableLandscapeMode = enableLandscapeMode;
        }
    }
    
    /**
     * Team Information nested class
     */
    public static class TeamInfo {
        private String teamName;
        private String sport = "basketball";
        private String league;
        private String season;
        private String homeTeam;
        private String awayTeam;
        private String venue;
        private LocalDateTime gameTime;
        
        // Getters and Setters
        public String getTeamName() {
            return teamName;
        }
        
        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }
        
        public String getSport() {
            return sport;
        }
        
        public void setSport(String sport) {
            this.sport = sport;
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
    }
}
