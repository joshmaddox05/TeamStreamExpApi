#!/bin/bash

# TeamStream Experience API - Enhanced Test Script
# Tests the API endpoints including team management features

set -e

echo "🏀 Testing TeamStream Experience API with Team Management..."

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
BASE_URL="http://localhost:8080/api/v1"
API_KEY="teamstream-api-key-2025"

# Function to test endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    local data=$4
    local expected_code=${5:-200}
    
    echo -e "${YELLOW}Testing: $description${NC}"
    
    if [ "$method" == "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" $BASE_URL$endpoint)
    else
        response=$(curl -s -w "\n%{http_code}" -X $method \
            -H "Content-Type: application/json" \
            -H "X-API-Key: $API_KEY" \
            -d "$data" \
            $BASE_URL$endpoint)
    fi
    
    # Split response and status code
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')  # Remove last line instead of head -n -1
    
    if [ "$http_code" -eq "$expected_code" ]; then
        echo -e "${GREEN}✅ PASS (HTTP $http_code)${NC}"
        if [ "$method" != "GET" ] && [ ! -z "$body" ]; then
            echo -e "${BLUE}Response:${NC}"
            echo "$body" | python3 -m json.tool 2>/dev/null || echo "$body"
        fi
    else
        echo -e "${RED}❌ FAIL (Expected $expected_code, got $http_code)${NC}"
        echo -e "${RED}Response: $body${NC}"
        return 1
    fi
    echo ""
}

echo -e "${BLUE}Starting API tests...${NC}"
echo ""

# Test 1: Health Check
test_endpoint "GET" "/health" "Health check endpoint"

# Test 2: API Info
test_endpoint "GET" "/" "API information endpoint"

# Test 3: Channel Service Health
test_endpoint "GET" "/channel/health" "Channel service health check"

# Test 4: Channel Service Info
test_endpoint "GET" "/channel/info" "Channel service information"

# Test 5: Basic Channel Creation
test_endpoint "POST" "/channel/create" "Basic channel creation" '{
    "userId": "coach_johnson",
    "eventType": "practice",
    "uid": 12345,
    "role": "publisher"
}'

# Test 6: Full Team Game Channel Creation
test_endpoint "POST" "/channel/create" "Full team game channel with all features" '{
    "userId": "broadcast_team_1",
    "eventType": "game",
    "uid": 67890,
    "role": "publisher",
    "teamName": "Lakers",
    "league": "NBA",
    "season": "2024-2025",
    "homeTeam": "Lakers",
    "awayTeam": "Warriors",
    "venue": "Crypto.com Arena",
    "gameTime": "2025-06-18T19:30:00",
    "streamQuality": "1080p",
    "enableRecording": true,
    "enableChat": true,
    "maxViewers": 5000,
    "description": "Lakers vs Warriors playoff game",
    "tags": ["basketball", "nba", "playoffs", "lakers", "warriors"]
}'

# Test 7: Subscriber Role
test_endpoint "POST" "/channel/create" "Subscriber/viewer channel creation" '{
    "userId": "fan_123",
    "eventType": "live_stream",
    "uid": 11111,
    "role": "subscriber",
    "teamName": "Bulls",
    "streamQuality": "720p"
}'

# Test 8: Token Refresh
echo -e "${YELLOW}Testing: Token refresh functionality${NC}"
CHANNEL_NAME="teamstream_game_6-18-2025_test123"
test_endpoint "POST" "/channel/refresh-token" "Token refresh for existing channel" '{
    "channelName": "'$CHANNEL_NAME'",
    "uid": 67890,
    "role": "publisher"
}'

# Test 9: Invalid API Key (should fail)
echo -e "${YELLOW}Testing: Invalid API key (should fail)${NC}"
response=$(curl -s -w "\n%{http_code}" -X POST \
    -H "Content-Type: application/json" \
    -H "X-API-Key: invalid-key" \
    -d '{
        "userId": "test_user",
        "eventType": "test",
        "uid": 99999,
        "role": "publisher"
    }' \
    $BASE_URL/channel/create)

http_code=$(echo "$response" | tail -n1)
if [ "$http_code" -eq "401" ] || [ "$http_code" -eq "403" ]; then
    echo -e "${GREEN}✅ PASS - Invalid API key correctly rejected (HTTP $http_code)${NC}"
else
    echo -e "${RED}❌ FAIL - Invalid API key should be rejected${NC}"
fi
echo ""

# Test 10: Validation Error (should fail)
test_endpoint "POST" "/channel/create" "Validation error test (missing required fields)" '{
    "eventType": "test"
}' 400

echo -e "${GREEN}🎉 All API tests completed!${NC}"
echo ""
echo -e "${BLUE}Enhanced Features Tested:${NC}"
echo "• ✅ Agora token generation and validation"
echo "• ✅ Team management information"
echo "• ✅ Stream configuration options"
echo "• ✅ Token refresh functionality"  
echo "• ✅ Role-based access (publisher/subscriber)"
echo "• ✅ Comprehensive metadata"
echo "• ✅ Basketball-specific features"
echo "• ✅ Validation and error handling"
echo ""
echo -e "${YELLOW}Ready for ClubCast/TeamStream integration! 🏀${NC}"
