package com.teamstream.expapi.service;

import com.teamstream.expapi.config.AgoraConfig;
import com.teamstream.expapi.config.ChannelConfig;
import com.teamstream.expapi.dto.ChannelRequest;
import com.teamstream.expapi.dto.ChannelResponse;
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
     * Creates a secure channel and generates an Agora RTC token
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

            logger.info("Created channel: {} for user: {} with UID: {}", 
                       channelName, request.getUserId(), request.getUid());

            return new ChannelResponse(
                channelName,
                token,
                agoraConfig.getAppId(),
                request.getUid(),
                request.getRole(),
                expirationTime
            );

        } catch (Exception e) {
            logger.error("Error creating channel for user: {}", request.getUserId(), e);
            throw new RuntimeException("Failed to create channel: " + e.getMessage(), e);
        }
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
}
