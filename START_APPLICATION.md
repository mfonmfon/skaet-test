# How to Start the USSD Application

## ✅ Configuration Files Created

I've created the missing configuration files:
- `src/main/resources/application.yml` - Production config (MySQL)
- `src/main/resources/application-dev.yml` - Development config (H2 in-memory)

---

## Quick Start (Easiest - No MySQL needed)

### Option 1: Run with H2 Database (Recommended for Testing)

```bash
# Start with development profile (uses H2 in-memory database)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Advantages:**
- ✅ No MySQL installation needed
- ✅ No database setup required
- ✅ Perfect for testing
- ✅ Data resets on restart (clean slate)

**Access H2 Console:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:ussd_banking`
- Username: `sa`
- Password: (leave empty)

---

## Option 2: Run with MySQL (Production)

### Step 1: Install and Start MySQL

**Windows:**
```bash
# Download MySQL from: https://dev.mysql.com/downloads/installer/
# Or use Docker:
docker run --name ussd-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=ussd_banking -p 3306:3306 -d mysql:8.0
```

**Linux/Mac:**
```bash
# Install MySQL
sudo apt-get install mysql-server  # Ubuntu/Debian
brew install mysql                  # Mac

# Or use Docker:
docker run --name ussd-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=ussd_banking -p 3306:3306 -d mysql:8.0
```

### Step 2: Update Database Credentials

Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ussd_banking?createDatabaseIfNotExist=true
    username: root
    password: your_password  # ← Change this
```

### Step 3: Start Application

```bash
mvn spring-boot:run
```

---

## Option 3: Docker Compose (Everything Included)

```bash
# Start both application and MySQL
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop
docker-compose down
```

---

## Verify Application is Running

### Test 1: Health Check
```bash
curl http://localhost:8080/api/v1/health
```

**Expected Response:**
```json
{
  "isSuccessful": true,
  "message": "Service is healthy",
  "status": 200,
  "data": "USSD Service is running"
}
```

### Test 2: USSD Endpoint
```bash
curl -X POST http://localhost:8080/api/v1/ussd \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "sessionId=test123&phoneNumber=%2B254712345678&text=&serviceCode=*384*7448#"
```

**Expected Response:**
```
CON Welcome to Mobile Banking
1. Check Balance
2. Deposit
3. Withdraw
4. Transaction History
```

---

## Configuration Explained

### application.yml (Production)

```yaml
server:
  port: 8080  # Application runs on port 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ussd_banking
    username: root
    password: password  # Change this!

  jpa:
    hibernate:
      ddl-auto: update  # Auto-create/update tables
```

### application-dev.yml (Development)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:ussd_banking  # In-memory database
    
  h2:
    console:
      enabled: true  # Access at /h2-console
```

---

## Troubleshooting

### Issue: Port 8080 already in use

**Solution 1: Change Port**

Edit `application.yml`:
```yaml
server:
  port: 8081  # Use different port
```

**Solution 2: Kill Process on Port 8080**

Windows:
```bash
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Linux/Mac:
```bash
lsof -i :8080
kill -9 <PID>
```

### Issue: MySQL Connection Failed

**Solution:** Use H2 instead (development profile):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Issue: Application won't start

**Check logs:**
```bash
# Logs are saved to: logs/application.log
tail -f logs/application.log
```

---

## Next Steps After Starting

### 1. Test Locally
```bash
# Run the test script
test-connection.bat  # Windows
./test-connection.sh # Linux/Mac
```

### 2. Expose with ngrok
```bash
# In a NEW terminal (keep app running)
ngrok http 8080

# Copy the HTTPS URL
# Example: https://abc123.ngrok.io
```

### 3. Configure Africa's Talking
1. Go to: https://developers.africastalking.com/simulator
2. Update Callback URL: `https://abc123.ngrok.io/api/v1/ussd`
3. Save

### 4. Test in Simulator
- Dial your USSD code
- Should work! ✅

---

## Quick Commands Reference

```bash
# Start with H2 (easiest)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Start with MySQL
mvn spring-boot:run

# Build JAR
mvn clean package

# Run JAR
java -jar target/ussdapp-0.0.1-SNAPSHOT.jar

# Run with specific profile
java -jar target/ussdapp-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Docker Compose
docker-compose up -d
docker-compose logs -f
docker-compose down
```

---

## Database Options Comparison

| Feature | H2 (Dev) | MySQL (Prod) |
|---------|----------|--------------|
| Setup | ✅ None needed | ❌ Install required |
| Speed | ✅ Very fast | ⚠️ Slower |
| Data Persistence | ❌ Lost on restart | ✅ Persisted |
| Web Console | ✅ Built-in | ❌ External tools |
| Best For | Testing, Development | Production |

---

## Recommended: Start with H2

For testing and development, use H2:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Why?**
- No database installation needed
- Faster startup
- Built-in web console
- Perfect for testing with Africa's Talking simulator

**Switch to MySQL later for production!**

---

## Summary

1. **Configuration files created** ✅
2. **Two profiles available:**
   - `dev` - H2 database (easy, no setup)
   - `default` - MySQL database (production)
3. **Start command:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
4. **Test it works:**
   ```bash
   curl http://localhost:8080/api/v1/health
   ```

**You're ready to go!** 🚀
