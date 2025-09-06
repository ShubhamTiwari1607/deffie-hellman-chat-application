# 🚀 **Diffie-Hellman Chat REST API Documentation**

## 📋 **Overview**

This document describes the REST API implementation for the Diffie-Hellman Chat Application. The API provides endpoints for user management, key generation, key exchange, and system health monitoring.

## 🌐 **Base URL**

```
http://localhost:8080/api/chat
```

## 🔑 **Authentication**

Currently, no authentication is required for demo purposes. In production, implement proper authentication and authorization.

## 📚 **API Endpoints**

### **1. User Management**

#### **Generate Keys for User**
```http
POST /api/chat/users/{userId}/keys
```

**Description:** Generates a new Diffie-Hellman key pair for the specified user.

**Path Parameters:**
- `userId` (string): The unique identifier for the user

**Response:**
```json
{
  "userId": "alice",
  "publicKey": "a1b2c3d4e5f6...",
  "message": "Key pair generated successfully"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/chat/users/alice/keys
```

#### **Get User's Public Key**
```http
GET /api/chat/users/{userId}/public-key
```

**Description:** Retrieves the public key for the specified user.

**Path Parameters:**
- `userId` (string): The unique identifier for the user

**Response:**
```json
{
  "userId": "alice",
  "publicKey": "a1b2c3d4e5f6..."
}
```

**Example:**
```bash
curl http://localhost:8080/api/chat/users/alice/public-key
```

#### **Get Online Users**
```http
GET /api/chat/users/online
```

**Description:** Returns a list of all currently online users with their public keys.

**Response:**
```json
[
  {
    "userId": "alice",
    "publicKey": "a1b2c3d4e5f6...",
    "isOnline": true
  },
  {
    "userId": "bob",
    "publicKey": "b2c3d4e5f6g7...",
    "isOnline": true
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/chat/users/online
```

#### **Remove User**
```http
DELETE /api/chat/users/{userId}
```

**Description:** Removes a user and their associated keys from the system.

**Path Parameters:**
- `userId` (string): The unique identifier for the user

**Response:**
```json
{
  "message": "User removed successfully",
  "userId": "alice"
}
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/chat/users/alice
```

### **2. Key Exchange**

#### **Perform Key Exchange**
```http
POST /api/chat/keys/exchange
```

**Description:** Performs Diffie-Hellman key exchange between two users to compute a shared secret.

**Request Body:**
```json
{
  "userId": "alice",
  "otherUserPublicKey": "b2c3d4e5f6g7..."
}
```

**Response (Success):**
```json
{
  "success": true,
  "sharedSecret": "c3d4e5f6g7h8...",
  "message": "Key exchange successful"
}
```

**Response (Error):**
```json
{
  "success": false,
  "error": "Invalid public key format. Must be a valid hexadecimal string."
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/chat/keys/exchange \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "alice",
    "otherUserPublicKey": "b2c3d4e5f6g7..."
  }'
```

#### **Get Diffie-Hellman Parameters**
```http
GET /api/chat/keys/parameters
```

**Description:** Returns the Diffie-Hellman parameters (P and G) used by the system.

**Response:**
```json
{
  "p": "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74...",
  "g": "2",
  "description": "RFC 3526 Diffie-Hellman parameters"
}
```

**Example:**
```bash
curl http://localhost:8080/api/chat/keys/parameters
```

### **3. Health and Status**

#### **Health Check**
```http
GET /api/chat/health
```

**Description:** Returns the health status of the API service.

**Response:**
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T12:00:00",
  "service": "Diffie-Hellman Chat API",
  "version": "1.0.0",
  "onlineUsers": 5
}
```

**Example:**
```bash
curl http://localhost:8080/api/chat/health
```

#### **API Information**
```http
GET /api/chat/info
```

**Description:** Returns general information about the API.

**Response:**
```json
{
  "name": "Diffie-Hellman Chat REST API",
  "version": "1.0.0",
  "description": "REST API for secure chat application using Diffie-Hellman key exchange",
  "endpoints": {
    "users": "/api/chat/users",
    "keys": "/api/chat/keys",
    "health": "/api/chat/health"
  },
  "documentation": "Available at /api/chat/info"
}
```

**Example:**
```bash
curl http://localhost:8080/api/chat/info
```

## 📖 **Documentation Endpoints**

### **API Documentation**
```http
GET /api/docs
```

**Description:** Returns comprehensive API documentation with examples.

### **API Examples**
```http
GET /api/docs/examples
```

**Description:** Returns practical examples for using the API.

## 🔧 **JavaScript Examples**

### **Generate Keys**
```javascript
fetch('/api/chat/users/alice/keys', {
    method: 'POST'
})
.then(response => response.json())
.then(data => console.log(data));
```

### **Exchange Keys**
```javascript
fetch('/api/chat/keys/exchange', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        userId: 'alice',
        otherUserPublicKey: 'b2c3d4e5f6g7...'
    })
})
.then(response => response.json())
.then(data => console.log(data));
```

### **Get Online Users**
```javascript
fetch('/api/chat/users/online')
.then(response => response.json())
.then(data => console.log(data));
```

## ⚠️ **Error Handling**

The API uses standard HTTP status codes and returns error responses in the following format:

```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid public key format. Must be a valid hexadecimal string.",
  "path": "/api/chat/keys/exchange"
}
```

### **Common HTTP Status Codes**

- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource conflict
- `500 Internal Server Error`: Server error

## 🌍 **CORS Configuration**

CORS is enabled for `http://localhost:3000` to allow frontend integration.

## 🔐 **Security Considerations**

1. **Key Validation**: All public keys are validated to ensure they are valid hexadecimal strings
2. **Input Sanitization**: User inputs are sanitized and validated
3. **Error Handling**: Sensitive information is not exposed in error messages
4. **Session Management**: User sessions are properly managed and cleaned up

## 🧪 **Testing the API**

### **Using curl**

1. **Test the backend is running:**
   ```bash
   curl http://localhost:8080/
   ```

2. **Generate keys for a user:**
   ```bash
   curl -X POST http://localhost:8080/api/chat/users/alice/keys
   ```

3. **Get the user's public key:**
   ```bash
   curl http://localhost:8080/api/chat/users/alice/public-key
   ```

4. **Check API health:**
   ```bash
   curl http://localhost:8080/api/chat/health
   ```

### **Using Postman or similar tools**

Import the following collection or use the endpoints above:

- **Base URL**: `http://localhost:8080`
- **Collection**: All endpoints under `/api/chat/*`

## 📁 **Project Structure**

```
backend/src/main/java/com/example/diffiehellmanchat/
├── controller/
│   ├── ChatRestController.java          # Main REST API controller
│   ├── ApiDocumentationController.java  # API documentation
│   └── TestController.java             # Basic test endpoints
├── service/
│   ├── DiffieHellmanService.java       # Key generation and exchange
│   └── UserSessionService.java         # User session management
├── dto/
│   ├── KeyExchangeRequest.java         # Key exchange request DTO
│   ├── KeyExchangeResponse.java        # Key exchange response DTO
│   └── UserInfo.java                   # User information DTO
├── model/
│   └── ChatMessage.java                # Chat message model
└── exception/
    └── GlobalExceptionHandler.java     # Global exception handling
```

## 🚀 **Getting Started**

1. **Start the Spring Boot backend:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **Test the API:**
   ```bash
   curl http://localhost:8080/api/chat/health
   ```

3. **View API documentation:**
   ```bash
   curl http://localhost:8080/api/docs
   ```

## 🔄 **Integration with WebSocket**

The REST API works alongside the WebSocket implementation:

- **REST API**: For key management, user operations, and system status
- **WebSocket**: For real-time chat functionality and live key exchange

## 📝 **Notes**

- All public keys are returned as hexadecimal strings
- The system uses RFC 3526 Diffie-Hellman parameters
- User sessions are automatically cleaned up when WebSocket connections close
- The API is designed to be stateless and scalable

