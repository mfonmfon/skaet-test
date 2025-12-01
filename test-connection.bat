@echo off
echo ==========================================
echo USSD Application Connection Test
echo ==========================================
echo.

REM Test 1: Check if application is running locally
echo Test 1: Checking if application is running on localhost:8080...
curl -s http://localhost:8080/api/v1/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Application is running
) else (
    echo [ERROR] Application is NOT running
    echo   Start with: mvn spring-boot:run
    exit /b 1
)
echo.

REM Test 2: Check health endpoint
echo Test 2: Testing health endpoint...
curl -s http://localhost:8080/api/v1/health
echo.
echo.

REM Test 3: Check USSD endpoint locally
echo Test 3: Testing USSD endpoint locally...
curl -s -X POST http://localhost:8080/api/v1/ussd -H "Content-Type: application/x-www-form-urlencoded" -d "sessionId=test123&phoneNumber=%%2B254712345678&text=&serviceCode=*384*7448#"
echo.
echo.

REM Test 4: Check if ngrok is running
echo Test 4: Checking for ngrok...
tasklist /FI "IMAGENAME eq ngrok.exe" 2>NUL | find /I /N "ngrok.exe">NUL
if %errorlevel% equ 0 (
    echo [OK] ngrok is running
    echo   Check ngrok terminal for your public URL
) else (
    echo [ERROR] ngrok is NOT running
    echo   Start with: ngrok http 8080
)
echo.

REM Summary
echo ==========================================
echo Summary
echo ==========================================
echo.
echo If all tests pass:
echo 1. Copy your ngrok HTTPS URL (e.g., https://abc123.ngrok.io)
echo 2. Update Africa's Talking callback URL to:
echo    https://your-ngrok-url.ngrok.io/api/v1/ussd
echo 3. Test in the simulator
echo.
echo If tests fail:
echo - Make sure application is running: mvn spring-boot:run
echo - Make sure ngrok is running: ngrok http 8080
echo - Check logs for errors
echo.
pause
