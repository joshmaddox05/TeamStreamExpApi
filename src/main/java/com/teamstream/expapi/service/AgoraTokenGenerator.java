package com.teamstream.expapi.service;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.CRC32;

/**
 * Agora RTC Token Generator
 * Implementation of Agora's token generation algorithm
 */
@Component
public class AgoraTokenGenerator {

    public enum Role {
        PUBLISHER(1),
        SUBSCRIBER(2);

        private final int value;

        Role(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static final String VERSION = "007";
    private static final int SERVICE_TYPE = 1; // RTC Service

    /**
     * Generates an Agora RTC token
     */
    public String generateToken(String appId, String appCertificate, String channelName, 
                               int uid, Role role, int expireTimestamp) {
        try {
            // Create message
            Message message = new Message();
            message.salt = (int) (System.currentTimeMillis() / 1000);
            message.ts = (int) (System.currentTimeMillis() / 1000);
            message.privileges.put(1, expireTimestamp); // Join channel privilege
            
            if (role == Role.PUBLISHER) {
                message.privileges.put(2, expireTimestamp); // Publish audio privilege
                message.privileges.put(3, expireTimestamp); // Publish video privilege
                message.privileges.put(4, expireTimestamp); // Publish data stream privilege
            }

            // Pack message
            byte[] messageBytes = packMessage(message, channelName, uid);
            
            // Generate signature
            String signature = generateSignature(appCertificate, appId, channelName, uid, messageBytes);
            
            // Pack token
            String token = VERSION + appId + signature + Base64.getEncoder().encodeToString(messageBytes);
            
            return token;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Agora token", e);
        }
    }

    private byte[] packMessage(Message message, String channelName, int uid) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        // Pack salt
        buffer.putInt(message.salt);
        
        // Pack timestamp
        buffer.putInt(message.ts);
        
        // Pack privileges
        buffer.putShort((short) message.privileges.size());
        for (java.util.Map.Entry<Integer, Integer> entry : message.privileges.entrySet()) {
            buffer.putShort(entry.getKey().shortValue());
            buffer.putInt(entry.getValue());
        }
        
        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        
        return result;
    }

    private String generateSignature(String appCertificate, String appId, String channelName, 
                                   int uid, byte[] message) throws Exception {
        
        // Create signing key
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(appCertificate.getBytes(), "HmacSHA256");
        mac.init(keySpec);
        
        // Create content to sign
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(appId.getBytes());
        buffer.put(channelName.getBytes());
        buffer.putInt(uid);
        buffer.put(message);
        
        byte[] content = new byte[buffer.position()];
        buffer.flip();
        buffer.get(content);
        
        // Generate signature
        byte[] signature = mac.doFinal(content);
        
        return Base64.getEncoder().encodeToString(signature);
    }

    private static class Message {
        int salt;
        int ts;
        java.util.Map<Integer, Integer> privileges = new java.util.HashMap<>();
    }
}
