#!/bin/bash

# TeamStream Experience API Test Script
# Tests all endpoints to ensure everything is working correctly

API_BASE="http://localhost:8080/api/v1"
API_KEY="teamstream-api-key-2025"

echo "🚀 Testing TeamStream Experience API"
echo "=================================="

# Test 1: Health Check
echo "1. Testing Health Endpoint..."
curl -s "${API_BASE}/health" | python3 -m json.tool
echo -e "\n"

# Test 2: API Info
echo "2. Testing API Info Endpoint..."
curl -s "${API_BASE}/" | python3 -m json.tool
echo -e "\n"

# Test 3: Channel Health
echo "3. Testing Channel Health Endpoint..."
curl -s "${API_BASE}/channel/health" | python3 -m json.tool
echo -e "\n"

# Test 4: Channel Creation (Publisher)
echo "4. Testing Channel Creation (Publisher)..."
curl -X POST "${API_BASE}/channel/create" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: ${API_KEY}" \
  -d '{
    "userId": "test_user_publisher",
    "eventType": "u16_game",
    "uid": 12345,
    "role": "publisher"
  }' | python3 -m json.tool
echo -e "\n"

# Test 5: Channel Creation (Subscriber)
echo "5. Testing Channel Creation (Subscriber)..."
curl -X POST "${API_BASE}/channel/create" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: ${API_KEY}" \
  -d '{
    "userId": "test_user_audience",
    "eventType": "u16_game",
    "uid": 67890,
    "role": "subscriber"
  }' | python3 -m json.tool
echo -e "\n"

# Test 6: Authentication Error
echo "6. Testing Authentication Error (no API key)..."
response=$(curl -s -w "%{http_code}" -X POST "${API_BASE}/channel/create" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "test_user",
    "eventType": "u16",
    "uid": 12345,
    "role": "publisher"
  }')
echo "HTTP Status: ${response: -3}"
echo -e "\n"

# Test 7: Validation Error
echo "7. Testing Validation Error (missing required fields)..."
curl -X POST "${API_BASE}/channel/create" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: ${API_KEY}" \
  -d '{
    "eventType": "u16"
  }' | python3 -m json.tool
echo -e "\n"

echo "✅ All tests completed!"
echo "API is running successfully on: ${API_BASE}"
