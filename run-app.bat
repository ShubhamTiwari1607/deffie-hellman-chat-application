@echo off
echo Starting Diffie-Hellman Chat Application...
echo.

echo Starting Backend (Spring Boot)...
start "Backend" cmd /k "cd backend && mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 10 /nobreak >nul

echo Starting Frontend (React)...
start "Frontend" cmd /k "cd frontend && npm start"

echo.
echo Applications are starting...
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Press any key to close this window...
pause >nul
