#!/bin/bash

echo "=========================================="
echo "USSD Application Connection Test"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test 1: Check if application is running locally
echo "Test 1: Checking if application is running on localhost:8080..."
if curl -s http://localhost:8080/api/v1/health > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Application is running${NC}"
else
    echo -e "${RED}✗ Application is NOT running${NC}"
    echo -e "${YELLOW}  → Start with: mvn spring-boot:run${NC}"
    exit 1
fi
echo ""

# Test 2: Check health endpoint
echo "Test 2: Testing health endpoint..."
HEALTH_RESPONSE=$(curl -s http://localhost:8080/api/v1/health)
if echo "$HEALTH_RESPONSE" | grep -q "isSuccessful"; then
    echo -e "${GREEN}✓ Health endpoint working${NC}"
    echo "  Response: $HEALTH_RESPONSE"
else
    echo -e "${RED}✗ Health endpoint not responding correctly${NC}"
fi
echo ""

# Test 3: Check USSD endpoint locally
echo "Test 3: Testing USSD endpoint locally..."
USSD_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/ussd \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "sessionId=test123&phoneNumber=%2B254712345678&text=&serviceCode=*384*7448#")

if echo "$USSD_RESPONSE" | grep -q "CON"; then
    echo -e "${GREEN}✓ USSD endpoint working${NC}"
    echo "  Response: $USSD_RESPONSE"
else
    echo -e "${RED}✗ USSD endpoint not responding correctly${NC}"
    echo "  Response: $USSD_RESPONSE"
fi
echo ""

# Test 4: Check if ngrok is running
echo "Test 4: Checking for ngrok..."
if pgrep -x "ngrok" > /dev/null; then
    echo -e "${GREEN}✓ ngrok is running${NC}"
    echo -e "${YELLOW}  → Check ngrok terminal for your public URL${NC}"
else
    echo -e "${RED}✗ ngrok is NOT running${NC}"
    echo -e "${YELLOW}  → Start with: ngrok http 8080${NC}"
fi
echo ""

# Summary
echo "=========================================="
echo "Summary"
echo "=========================================="
echo ""
echo "If all tests pass:"
echo "1. Copy your ngrok HTTPS URL (e.g., https://abc123.ngrok.io)"
echo "2. Update Africa's Talking callback URL to:"
echo "   https://your-ngrok-url.ngrok.io/api/v1/ussd"
echo "3. Test in the simulator"
echo ""
echo "If tests fail:"
echo "- Make sure application is running: mvn spring-boot:run"
echo "- Make sure ngrok is running: ngrok http 8080"
echo "- Check logs for errors"
echo ""
