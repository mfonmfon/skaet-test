# Bad Gateway (502) Error - Fix Guide

## What is Bad Gateway?

A **502 Bad Gateway** error means Africa's Talking simulator cannot connect to your application. This is a **connectivity issue**, not a code problem.

## Common Causes & Solutions

### 1. ✅ Application Not Running

**Check if your application is running:**

```bash
# Check if the application is running
curl http://localhost:8080/api/v1/health

# If you get a response, the app is running
# If you get "Connection refused", the app is NOT running
```

**Solution:**
```bash
# Start the application
mvn spring-boot:run

# Or if using JAR
java -jar target/ussdapp-0.0.1-SNAPSHOT.jar
```

---

### 2. ✅ ngrok Not Running or Expired

**Problem:** ngrok tunnel is not active or the URL expired

**Check ngrok status:**
```bash
# In a separate terminal, check if ngrok is running
# You should see something like: https://abc123.ngrok.io
```

**Solution:**
```bash
# Start ngrok (in a NEW terminal, keep app running)
ngrok http 8080

# Copy the HTTPS URL (e.g., https://abc123.ngrok.io)
# Update Africa's Talking callback URL with this new URL
```

**Important:** 
- Free ngrok URLs expire when you restart ngrok
- You must update the callback URL in Africa's Talking dashboard each time

---

### 3. ✅ Wrong Callback URL in Africa's Talking

**Problem:** The callback URL in Africa's Talking doesn't match your ngrok URL

**Solution:**

1. **Get your ngrok URL:**
   ```bash
   # Look at ngrok terminal output
   # Copy the HTTPS URL: https://abc123.ngrok.io
   ```

2. **Update Africa's Talking:**
   - Go to: https://developers.africastalking.com/simulator
   - Click on your USSD channel settings
   - Update Callback URL to: `https://abc123.ngrok.io/api/v1/ussd`
   - **Important:** Use `/api/v1/ussd` NOT `/api/ussd`
   - Save changes

---

### 4. ✅ Firewall Blocking Requests

**Problem:** Your firewall is blocking incoming requests

**Solution:**
```bash
# Windows: Allow Java through firewall
# Go to Windows Defender Firewall → Allow an app

# Or temporarily disable firewall for testing
# (Not recommended for production)
```

---

### 5. ✅ Wrong Port

**Problem:** Application is running on a different port

**Check application.yml:**
```yaml
server:
  port: 8080  # Make sure this is 8080
```

**Verify port:**
```bash
# Check what's running on port 8080
netstat -ano | findstr :8080

# Or test the endpoint
curl http://localhost:8080/api/v1/health
```

---

## Step-by-Step Fix

### Step 1: Verify Application is Running

```bash
# Terminal 1: Start application
mvn spring-boot:run

# Wait for: "Started UssdappApplication in X seconds"
```

### Step 2: Test Locally

```bash
# In another terminal, test the endpoint
curl -X POST http://localhost:8080/api/v1/ussd \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "sessionId=test&phoneNumber=%2B254712345678&text=&serviceCode=*384*7448#"

# Expected: CON Welcome to Mobile Banking...
# If this works, your app is fine!
```

### Step 3: Start ngrok

```bash
# Terminal 2: Start ngrok
ngrok http 8080

# You should see output like:
# Forwarding  https://abc123.ngrok.io -> http://localhost:8080
```

### Step 4: Test ngrok URL

```bash
# Test the ngrok URL
curl -X POST https://abc123.ngrok.io/api/v1/ussd \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "sessionId=test&phoneNumber=%2B254712345678&text=&serviceCode=*384*7448#"

# Expected: CON Welcome to Mobile Banking...
# If this works, ngrok is working!
```

### Step 5: Update Africa's Talking

1. Go to: https://developers.africastalking.com/simulator
2. Find your USSD channel
3. Update Callback URL: `https://abc123.ngrok.io/api/v1/ussd`
4. Save

### Step 6: Test in Simulator

1. Dial your USSD code in the simulator
2. Should work now! ✅

---

## Quick Diagnostic Commands

### Check if Application is Running
```bash
curl http://localhost:8080/api/v1/health
```
**Expected:** `{"isSuccessful":true,"message":"Service is healthy"...}`

### Check if USSD Endpoint Works Locally
```bash
curl -X POST http://localhost:8080/api/v1/ussd \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "sessionId=test&phoneNumber=%2B254712345678&text=&serviceCode=*384*7448#"
```
**Expected:** `CON Welcome to Mobile Banking...`

### Check if ngrok is Working
```bash
curl https://your-ngrok-url.ngrok.io/api/v1/health
```
**Expected:** Same as local health check

---

## Common Mistakes

### ❌ Wrong Callback URL Format

**Wrong:**
- `http://abc123.ngrok.io/api/v1/ussd` (HTTP instead of HTTPS)
- `https://abc123.ngrok.io/ussd` (Missing /api/v1)
- `https://abc123.ngrok.io/api/ussd` (Wrong path)
- `https://localhost:8080/api/v1/ussd` (localhost won't work)

**Correct:**
- `https://abc123.ngrok.io/api/v1/ussd` ✅

### ❌ ngrok URL Expired

**Problem:** Free ngrok URLs change every time you restart ngrok

**Solution:** 
- Get a new URL each time: `ngrok http 8080`
- Update Africa's Talking callback URL
- Or upgrade to ngrok paid plan for static URLs

### ❌ Application Not Fully Started

**Problem:** Testing before application finishes starting

**Solution:** Wait for this message:
```
Started UssdappApplication in X.XXX seconds
```

---

## Production Deployment (No ngrok needed)

For production, deploy to a server with a public IP:

### Option 1: Deploy to Cloud (Heroku, AWS, etc.)

```bash
# Build JAR
mvn clean package

# Deploy to your server
# Get public URL (e.g., https://your-app.herokuapp.com)

# Update Africa's Talking callback:
# https://your-app.herokuapp.com/api/v1/ussd
```

### Option 2: Deploy to VPS with Domain

```bash
# Deploy to your VPS
# Configure nginx/apache
# Get SSL certificate (Let's Encrypt)

# Update Africa's Talking callback:
# https://yourdomain.com/api/v1/ussd
```

---

## Troubleshooting Checklist

- [ ] Application is running (`mvn spring-boot:run`)
- [ ] Application responds locally (`curl http://localhost:8080/api/v1/health`)
- [ ] USSD endpoint works locally (test with curl)
- [ ] ngrok is running (`ngrok http 8080`)
- [ ] ngrok URL is accessible (test with curl)
- [ ] Callback URL in Africa's Talking is correct
- [ ] Callback URL uses HTTPS (not HTTP)
- [ ] Callback URL includes `/api/v1/ussd`
- [ ] No firewall blocking port 8080
- [ ] Waited for application to fully start

---

## Still Getting Bad Gateway?

### Check Application Logs

```bash
# Check for errors in logs
tail -f logs/application.log

# Or if running in terminal, check console output
```

### Verify Endpoint Path

The endpoint MUST be: `/api/v1/ussd`

Check your controller:
```java
@RestController
@RequestMapping("/api/v1")  // ← Must be /api/v1
public class UssdController {
    
    @PostMapping("/ussd")  // ← Combined: /api/v1/ussd
    public ResponseEntity<String> handleUssdRequest(UssdRequest request) {
        // ...
    }
}
```

### Test with Postman

1. Open Postman
2. Create POST request
3. URL: `https://your-ngrok-url.ngrok.io/api/v1/ussd`
4. Headers: `Content-Type: application/x-www-form-urlencoded`
5. Body (x-www-form-urlencoded):
   - `sessionId`: `test123`
   - `phoneNumber`: `+254712345678`
   - `text`: `` (empty)
   - `serviceCode`: `*384*7448#`
6. Send

**Expected:** `CON Welcome to Mobile Banking...`

---

## Summary

**Bad Gateway = Connectivity Issue**

Most common causes:
1. ✅ Application not running → Start it
2. ✅ ngrok not running → Start it
3. ✅ Wrong callback URL → Update it
4. ✅ ngrok URL expired → Get new one and update

**Quick Fix:**
```bash
# Terminal 1
mvn spring-boot:run

# Terminal 2
ngrok http 8080

# Copy ngrok HTTPS URL
# Update Africa's Talking callback URL
# Test in simulator
```

**The application code is fine - it's just a connection issue!** 🔧
