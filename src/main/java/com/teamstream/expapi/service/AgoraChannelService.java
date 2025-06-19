package com.teamstream.expapi.service;

import com.teamstream.expapi.config.AgoraConfig;
import com.teamstream.expapi.config.ChannelConfig;
import com.teamstream.expapi.dto.ChannelRequest;
import com.teamstream.expapi.dto.ChannelResponse;
import com.teamstream.expapi.dto.TokenRefreshRequest;
import com.teamstream.expapi.service.AgoraTokenGenerator.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service for managing Agora channels and tokens
 */
@Service
public class AgoraChannelService {

    private static final Logger logger = LoggerFactory.getLogger(AgoraChannelService.class);

    private final AgoraConfig agoraConfig;
    private final ChannelConfig channelConfig;
    private final AgoraTokenGenerator tokenGenerator;

    @Autowired
    public AgoraChannelService(AgoraConfig agoraConfig, ChannelConfig channelConfig, 
                              AgoraTokenGenerator tokenGenerator) {
        this.agoraConfig = agoraConfig;
        this.channelConfig = channelConfig;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * Creates a secure channel and generates an Agora RTC token with team management features
     */
    public ChannelResponse createChannel(ChannelRequest request) {
        try {
            // Generate secure channel name
            String channelName = generateChannelName(request.getEventType());
            
            // Get current timestamp and calculate expiration
            int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
            int privilegeExpiredTs = currentTimestamp + agoraConfig.getToken().getExpiration();
            
            // Determine role
            Role role = determineRole(request.getRole());
            
            // Generate Agora RTC token
            String token = tokenGenerator.generateToken(
                agoraConfig.getAppId(),
                agoraConfig.getAppCertificate(),
                channelName,
                request.getUid(),
                role,
                privilegeExpiredTs
            );

            // Calculate expiration time
            LocalDateTime expirationTime = LocalDateTime.now()
                .plusSeconds(agoraConfig.getToken().getExpiration());

            logger.info("Created channel: {} for user: {} with UID: {} (Team: {})", 
                       channelName, request.getUserId(), request.getUid(), request.getTeamName());

            // Create enhanced response with team management features
            ChannelResponse response = new ChannelResponse(
                channelName,
                token,
                agoraConfig.getAppId(),
                request.getUid(),
                request.getRole(),
                expirationTime,
                request.getUserId(),
                request.getEventType()
            );
            
            // Set stream configuration
            configureStreamSettings(response, request);
            
            // Set team information
            configureTeamInfo(response, request);
            
            // Add metadata
            addMetadata(response, request, channelName);

            return response;

        } catch (Exception e) {
            logger.error("Error creating channel for user: {}", request.getUserId(), e);
            throw new RuntimeException("Failed to create channel: " + e.getMessage(), e);
        }
    }
    
    /**
     * Configures stream settings based on request
     */
    private void configureStreamSettings(ChannelResponse response, ChannelRequest request) {
        ChannelResponse.StreamConfig streamConfig = response.getStreamConfig();
        
        // Set video quality profile
        streamConfig.setVideoProfile(request.getStreamQuality());
        
        // Configure recording
        streamConfig.setEnableRecording(request.isEnableRecording());
        
        // Configure chat
        streamConfig.setEnableChat(request.isEnableChat());
        
        // Set max viewers
        streamConfig.setMaxViewers(request.getMaxViewers());
        
        // Set stream type based on event type
        String streamType = determineStreamType(request.getEventType());
        response.setStreamType(streamType);
        response.setStreamQuality(request.getStreamQuality());
        
        logger.debug("Configured stream settings - Quality: {}, Recording: {}, Max Viewers: {}", 
                    request.getStreamQuality(), request.isEnableRecording(), request.getMaxViewers());
    }
    
    /**
     * Configures team information
     */
    private void configureTeamInfo(ChannelResponse response, ChannelRequest request) {
        ChannelResponse.TeamInfo teamInfo = response.getTeamInfo();
        
        teamInfo.setTeamName(request.getTeamName());
        teamInfo.setLeague(request.getLeague());
        teamInfo.setSeason(request.getSeason());
        teamInfo.setHomeTeam(request.getHomeTeam());
        teamInfo.setAwayTeam(request.getAwayTeam());
        teamInfo.setVenue(request.getVenue());
        teamInfo.setGameTime(request.getGameTime());
        
        logger.debug("Configured team info - Team: {}, League: {}, Venue: {}", 
                    request.getTeamName(), request.getLeague(), request.getVenue());
    }
    
    /**
     * Adds metadata to the response
     */
    private void addMetadata(ChannelResponse response, ChannelRequest request, String channelName) {
        // Add technical metadata
        response.addMetadata("sdk_version", "4.x");
        response.addMetadata("api_version", "v1");
        response.addMetadata("channel_prefix", channelConfig.getPrefix());
        response.addMetadata("created_timestamp", System.currentTimeMillis());
        
        // Add stream metadata
        response.addMetadata("description", request.getDescription());
        response.addMetadata("tags", request.getTags());
        
        // Add basketball-specific metadata
        response.addMetadata("sport", "basketball");
        response.addMetadata("app_name", "TeamStream");
        response.addMetadata("channel_pattern", "teamstream_eventType_date_randomId");
        
        // Add useful client information
        response.addMetadata("camera_switching_enabled", true);
        response.addMetadata("landscape_mode_enabled", true);
        response.addMetadata("fullscreen_supported", true);
        
        logger.debug("Added metadata to channel response");
    }
    
    /**
     * Determines stream type based on event type
     */
    private String determineStreamType(String eventType) {
        if (eventType == null) return "live_stream";
        
        return switch (eventType.toLowerCase()) {
            case "game", "match", "competition" -> "live_game";
            case "practice", "training" -> "practice_session";
            case "interview", "press" -> "interview";
            case "replay", "highlight" -> "replay";
            default -> "live_stream";
        };
    }

    /**
     * Generates a secure channel name following the pattern: prefix_eventType_date_randomId
     * Example: mileacademy_u16_6-14-2025_abc123
     */
    private String generateChannelName(String eventType) {
        // Get current date
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(channelConfig.getDate().getFormat());
        String dateStr = now.format(formatter);
        
        // Generate random suffix for uniqueness
        String randomSuffix = generateRandomString(6);
        
        // Construct channel name
        String channelName = String.format("%s_%s_%s_%s",
            channelConfig.getPrefix(),
            sanitizeEventType(eventType),
            dateStr,
            randomSuffix
        );
        
        logger.debug("Generated channel name: {}", channelName);
        return channelName;
    }

    /**
     * Sanitizes event type to be URL/channel name safe
     */
    private String sanitizeEventType(String eventType) {
        return eventType.toLowerCase()
                       .replaceAll("[^a-z0-9]", "")
                       .substring(0, Math.min(eventType.length(), 10));
    }

    /**
     * Generates a random alphanumeric string
     */
    private String generateRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            result.append(chars.charAt(index));
        }
        
        return result.toString();
    }

    /**
     * Determines Agora role from string
     */
    private Role determineRole(String roleStr) {
        if (roleStr == null) {
            return Role.PUBLISHER;
        }
        
        return switch (roleStr.toLowerCase()) {
            case "subscriber", "audience" -> Role.SUBSCRIBER;
            case "publisher", "broadcaster", "host" -> Role.PUBLISHER;
            default -> Role.PUBLISHER;
        };
    }

    /**
     * Validates if the provided Agora configuration is valid
     */
    public boolean isConfigurationValid() {
        return agoraConfig.getAppId() != null && 
               !agoraConfig.getAppId().trim().isEmpty() &&
               !agoraConfig.getAppId().equals("your_agora_app_id_here") &&
               agoraConfig.getAppCertificate() != null && 
               !agoraConfig.getAppCertificate().trim().isEmpty() &&
               !agoraConfig.getAppCertificate().equals("your_agora_app_certificate_here");
    }

    /**
     * Refreshes an existing token for a channel
     */
    public ChannelResponse refreshToken(TokenRefreshRequest request) {
        try {
            // Get current timestamp and calculate expiration
            int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
            int privilegeExpiredTs = currentTimestamp + agoraConfig.getToken().getExpiration();
            
            // Determine role
            Role role = determineRole(request.getRole());
            
            // Generate new Agora RTC token
            String token = tokenGenerator.generateToken(
                agoraConfig.getAppId(),
                agoraConfig.getAppCertificate(),
                request.getChannelName(),
                request.getUid(),
                role,
                privilegeExpiredTs
            );

            // Calculate expiration time
            LocalDateTime expirationTime = LocalDateTime.now()
                .plusSeconds(agoraConfig.getToken().getExpiration());

            logger.info("Refreshed token for channel: {} with UID: {}", 
                       request.getChannelName(), request.getUid());

            // Create response with refreshed token
            ChannelResponse response = new ChannelResponse();
            response.setChannelName(request.getChannelName());
            response.setToken(token);
            response.setAppId(agoraConfig.getAppId());
            response.setUid(request.getUid());
            response.setRole(request.getRole());
            response.setExpirationTime(expirationTime);
            
            // Add refresh metadata
            response.addMetadata("token_refreshed", true);
            response.addMetadata("refresh_timestamp", System.currentTimeMillis());
            response.addMetadata("previous_token_expired", true);

            return response;

        } catch (Exception e) {
            logger.error("Error refreshing token for channel: {}", request.getChannelName(), e);
            throw new RuntimeException("Failed to refresh token: " + e.getMessage(), e);
        }
    }
}
