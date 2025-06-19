package com.teamstream.expapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "agora")
public class AgoraConfig {
    
    private String appId;
    private String appCertificate;
    private Token token = new Token();
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getAppCertificate() {
        return appCertificate;
    }
    
    public void setAppCertificate(String appCertificate) {
        this.appCertificate = appCertificate;
    }
    
    public Token getToken() {
        return token;
    }
    
    public void setToken(Token token) {
        this.token = token;
    }
    
    public static class Token {
        private int expiration = 86400; // 24 hours default
        
        public int getExpiration() {
            return expiration;
        }
        
        public void setExpiration(int expiration) {
            this.expiration = expiration;
        }
    }
}
