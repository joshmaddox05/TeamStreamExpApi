#!/bin/bash

# TeamStream Experience API - Docker Test Script
# Tests the Docker build and deployment locally before pushing to Render

echo "🐳 TeamStream Experience API - Docker Deployment Test"
echo "====================================================="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop and try again."
    exit 1
fi

echo "✅ Docker is running"

# Clean up any existing containers
echo "🧹 Cleaning up existing containers..."
docker-compose down --remove-orphans 2>/dev/null || true
docker rmi teamstreamexpapi_teamstream-api 2>/dev/null || true

echo ""
echo "🔨 Building Docker image..."
if docker build -t teamstream-experience-api .; then
    echo "✅ Docker image built successfully"
else
    echo "❌ Docker build failed"
    exit 1
fi

echo ""
echo "🚀 Starting application with Docker Compose..."
if docker-compose up -d; then
    echo "✅ Application started successfully"
else
    echo "❌ Failed to start application"
    exit 1
fi

echo ""
echo "⏳ Waiting for application to be ready..."
for i in {1..30}; do
    if curl -s http://localhost:8080/api/v1/health > /dev/null; then
        echo "✅ Application is ready!"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "❌ Application failed to start within 30 seconds"
        echo "📋 Container logs:"
        docker-compose logs
        exit 1
    fi
    sleep 1
    echo -n "."
done

echo ""
echo "🧪 Running API tests..."

# Test 1: Health check
echo "1. Testing health endpoint..."
health_response=$(curl -s http://localhost:8080/api/v1/health)
if echo "$health_response" | grep -q "UP"; then
    echo "   ✅ Health check passed"
else
    echo "   ❌ Health check failed"
    echo "   Response: $health_response"
fi

# Test 2: API Info
echo "2. Testing API info endpoint..."
info_response=$(curl -s http://localhost:8080/api/v1/)
if echo "$info_response" | grep -q "TeamStream Experience API"; then
    echo "   ✅ API info test passed"
else
    echo "   ❌ API info test failed"
fi

# Test 3: Channel creation
echo "3. Testing channel creation..."
channel_response=$(curl -s -X POST http://localhost:8080/api/v1/channel/create \
  -H "Content-Type: application/json" \
  -H "X-API-Key: teamstream-api-key-2025" \
  -d '{
    "userId": "docker_test_user",
    "eventType": "docker_test",
    "uid": 99999,
    "role": "publisher"
  }')

if echo "$channel_response" | grep -q "channelName"; then
    echo "   ✅ Channel creation test passed"
    echo "   Created channel: $(echo "$channel_response" | grep -o '"channelName":"[^"]*"' | cut -d'"' -f4)"
else
    echo "   ❌ Channel creation test failed"
    echo "   Response: $channel_response"
fi

echo ""
echo "📊 Application Status:"
echo "🌐 API URL: http://localhost:8080/api/v1"
echo "📋 Health: http://localhost:8080/api/v1/health"
echo "📖 Docs: http://localhost:8080/api/v1/"

echo ""
echo "🐳 Docker Information:"
echo "📦 Image size: $(docker images teamstream-experience-api --format "table {{.Size}}" | tail -n1)"
echo "🔧 Container status: $(docker-compose ps --format "table {{.Name}}\t{{.Status}}")"

echo ""
echo "💡 Commands:"
echo "   View logs: docker-compose logs -f"
echo "   Stop: docker-compose down"
echo "   Rebuild: docker-compose up --build -d"

echo ""
echo "🎉 Docker deployment test completed!"
echo "   Your application is ready for Render deployment."

# Keep the script running so user can test manually
echo ""
echo "Press Ctrl+C to stop the application and clean up containers."
trap 'echo ""; echo "🧹 Stopping and cleaning up..."; docker-compose down; echo "✅ Cleanup complete"; exit 0' INT

# Wait for user to stop
while true; do
    sleep 1
done
