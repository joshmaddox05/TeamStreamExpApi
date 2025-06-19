<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# TeamStream Experience API - Copilot Instructions

This is a Java Spring Boot 3.2.0 application using Java 21 and Gradle for build management. The API provides live streaming functionality with Agora SDK integration.

## Project Structure
- **Language**: Java 21
- **Framework**: Spring Boot 3.2.0
- **Build Tool**: Gradle
- **Architecture**: RESTful API with layered architecture

## Key Components
1. **Controllers**: Handle HTTP requests and responses
2. **Services**: Business logic and Agora SDK integration
3. **DTOs**: Data transfer objects for API communication
4. **Configuration**: Spring configuration classes for Agora and security

## Agora SDK Integration
- Uses Agora's authentication library for RTC token generation
- Generates secure channel names with format: `prefix_eventType_date_randomId`
- Supports both publisher and subscriber roles
- Token expiration management

## Security
- API Key authentication using custom filter
- CORS enabled for cross-origin requests
- Stateless session management

## Best Practices
- Follow Spring Boot conventions
- Use proper validation annotations
- Implement comprehensive error handling
- Include detailed logging
- Follow RESTful API design principles

## Environment Variables
- `AGORA_APP_ID`: Your Agora application ID
- `AGORA_APP_CERTIFICATE`: Your Agora application certificate
- `API_KEY`: API key for authentication

## Testing
- Use JUnit 5 for unit tests
- Include integration tests for controllers
- Mock external dependencies (Agora SDK) in tests
