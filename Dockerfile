# TeamStream Experience API - Multi-stage Dockerfile for Render deployment

# Build stage
FROM gradle:8.10.2-jdk21-alpine AS builder

WORKDIR /app

# Copy gradle configuration files
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle/ ./gradle/

# Download dependencies (this layer will be cached if dependencies don't change)
RUN gradle dependencies --no-daemon

# Copy source code
COPY src/ ./src/

# Build the application
RUN gradle bootJar --no-daemon

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN addgroup --system teamstream && adduser --system --group teamstream

# Copy the built JAR from builder stage
COPY --from=builder /app/build/libs/TeamStreamExpApi-*.jar app.jar

# Change ownership to non-root user
RUN chown teamstream:teamstream app.jar

# Switch to non-root user
USER teamstream

# Expose the port that the app runs on
EXPOSE 8080

# Environment variables with defaults
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/v1/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
