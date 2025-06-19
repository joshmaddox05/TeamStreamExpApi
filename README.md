# TeamStream Experience API

A Java Spring Boot API for live streaming applications with Agora SDK integration. This API generates secure channel names and Agora RTC tokens for seamless live streaming experiences.

## Features

- 🔐 **Secure Channel Generation**: Creates unique channel names with format `mileacademy_eventType_date_randomId`
- 🎫 **Agora Token Management**: Generates RTC tokens with configurable expiration times
- 🛡️ **API Key Authentication**: Secure endpoint access with custom API key validation
- 📊 **Health Monitoring**: Built-in health check endpoints
- 🌐 **CORS Support**: Cross-origin request handling for web applications
- ⚡ **High Performance**: Built with Java 21 and Spring Boot 3.2.0

## API Endpoints

### Public Endpoints
- `GET /api/v1/` - API information and documentation
- `GET /api/v1/health` - Health check

### Protected Endpoints (Require X-API-Key header)
- `POST /api/v1/channel/create` - Create channel and generate token
- `GET /api/v1/channel/health` - Channel service health check
- `GET /api/v1/channel/info` - Channel service information

## Quick Start

### Prerequisites
- Java 21
- Agora.io account with App ID and App Certificate

### 1. Clone and Setup
```bash
git clone <your-repo-url>
cd TeamStreamExpApi
```

### 2. Configure Environment Variables
Your Agora credentials are already configured in the application:
```bash
# Already configured for TeamStream project
AGORA_APP_ID=a111a0c2193f4ecaa04d6e74190f082d
AGORA_APP_CERTIFICATE=ae0634149a0747d1807db13fcdeca6ae
API_KEY=teamstream-api-key-2025
```

For production, set these as environment variables:
```bash
export AGORA_APP_ID="a111a0c2193f4ecaa04d6e74190f082d"
export AGORA_APP_CERTIFICATE="ae0634149a0747d1807db13fcdeca6ae"
export API_KEY="your_production_api_key"
```

### 3. Build and Run
```bash
# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

The API will be available at `http://localhost:8080/api/v1`

## Usage Example

### Create a Channel
```bash
curl -X POST http://localhost:8080/api/v1/channel/create \
  -H "Content-Type: application/json" \
  -H "X-API-Key: teamstream-api-key-2025" \
  -d '{
    "userId": "user123",
    "eventType": "u16",
    "uid": 12345,
    "role": "publisher"
  }'
```

### Response
```json
{
  "channelName": "teamstream_u16_6-18-2025_abc123",
  "token": "007a111a0c2193f4ecaa04d6e74190f082d...",
  "appId": "a111a0c2193f4ecaa04d6e74190f082d",
  "uid": 12345,
  "role": "publisher",
  "expirationTime": "2025-06-19 19:16:53",
  "createdAt": "2025-06-18 19:16:53"
}
```

### Quick Test
Run the provided test script:
```bash
./test-api.sh
```

## Configuration

### Application Properties
Key configuration options in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api/v1

# Agora Configuration
agora.app-id=${AGORA_APP_ID}
agora.app-certificate=${AGORA_APP_CERTIFICATE}
agora.token.expiration=86400

# Channel Configuration
channel.prefix=mileacademy
channel.date.format=M-d-yyyy

# Security
security.api-key=${API_KEY}
```

### Channel Name Format
Channels are automatically generated with this secure format:
- **Prefix**: "teamstream" (configured for your project)
- **Event Type**: Sanitized user input
- **Date**: Current date in M-d-yyyy format
- **Random Suffix**: 6-character alphanumeric string

Example: `teamstream_u16_6-18-2025_abc123`

## Security

### API Key Authentication
All protected endpoints require an `X-API-Key` header:
```bash
curl -H "X-API-Key: teamstream-api-key-2025" ...
```

**Note**: Change the API key for production use!

### CORS Configuration
CORS is enabled for all origins. In production, configure specific origins in `SecurityConfig.java`.

## Development

### Project Structure
```
src/main/java/com/teamstream/expapi/
├── TeamStreamExpApiApplication.java    # Main application class
├── config/                            # Configuration classes
│   ├── AgoraConfig.java              # Agora SDK configuration
│   ├── ChannelConfig.java            # Channel generation settings
│   ├── SecurityConfig.java           # Security configuration
│   └── ApiKeyAuthenticationFilter.java # Custom auth filter
├── controller/                        # REST controllers
│   ├── ChannelController.java        # Channel management endpoints
│   └── HealthController.java         # Health check endpoints
├── dto/                              # Data transfer objects
│   ├── ChannelRequest.java           # Channel creation request
│   ├── ChannelResponse.java          # Channel creation response
│   └── ErrorResponse.java            # Error response format
└── service/                          # Business logic
    └── AgoraChannelService.java      # Agora integration service
```

### Building
```bash
# Clean and build
./gradlew clean build

# Run tests
./gradlew test

# Run application
./gradlew bootRun

# Create executable JAR
./gradlew bootJar
```

### Testing
```bash
# Health check
curl http://localhost:8080/api/v1/health

# API info
curl http://localhost:8080/api/v1/
```

## Agora SDK Integration

This API uses Agora's RTC SDK for token generation. Key features:

- **Dynamic Channels**: Channels are created automatically when users join
- **Token Security**: Tokens include expiration time and role-based access
- **Role Support**: Publisher (broadcaster) and Subscriber (audience) roles
- **UID Management**: Each user gets a unique identifier for the session

## Deployment

### Docker (Optional)
```dockerfile
FROM openjdk:21-jdk-slim
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables for Production
```bash
AGORA_APP_ID=your_production_app_id
AGORA_APP_CERTIFICATE=your_production_certificate
API_KEY=your_secure_production_key
SPRING_PROFILES_ACTIVE=prod
```

## Troubleshooting

### Common Issues

1. **Invalid Agora Configuration**
   - Ensure `AGORA_APP_ID` and `AGORA_APP_CERTIFICATE` are set correctly
   - Check the health endpoint: `GET /api/v1/channel/health`

2. **Authentication Errors**
   - Verify `X-API-Key` header is included in requests
   - Ensure the API key matches the configured value

3. **Token Generation Failures**
   - Check Agora SDK dependency is properly loaded
   - Verify App Certificate is correctly configured

### Logging
Enable debug logging by adding to application.properties:
```properties
logging.level.com.teamstream.expapi=DEBUG
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues related to:
- **Agora SDK**: Check [Agora Documentation](https://docs.agora.io/)
- **Spring Boot**: Check [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- **This API**: Open an issue in this repository
