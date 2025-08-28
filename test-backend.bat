@echo off
echo Testing Diffie-Hellman Chat Backend...
echo.

echo Starting Spring Boot application...
cd backend
mvn spring-boot:run

echo.
echo Backend should be running at http://localhost:8080
echo Test endpoints:
echo - http://localhost:8080/
echo - http://localhost:8080/health
echo.
echo Press Ctrl+C to stop the backend
