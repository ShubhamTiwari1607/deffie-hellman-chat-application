# 🔐 Diffie-Hellman Secure Chat Application

A real-time secure chat application that demonstrates the Diffie-Hellman key exchange protocol for secure communication. Built with Spring Boot backend and React.js frontend.

## 🌟 Features

- **Real-time Chat**: WebSocket-based instant messaging
- **Diffie-Hellman Key Exchange**: Secure key generation and exchange
- **Modern UI**: Beautiful, responsive design with glassmorphism effects
- **User Authentication**: Simple username-based login system
- **Key Management**: Visual display of public keys and shared secrets
- **Cross-platform**: Works on desktop and mobile devices

## 🏗️ Architecture

### Backend (Spring Boot)
- **Spring Boot 3.2.0** with Java 17
- **WebSocket Support** for real-time communication
- **Diffie-Hellman Service** for cryptographic operations
- **RESTful API** for key management

### Frontend (React.js)
- **React 18** with modern hooks
- **Native WebSocket Client** for real-time communication
- **Responsive Design** with CSS Grid and Flexbox
- **Real-time Updates** for messages and keys

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6 or higher

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm start
   ```

   The frontend will open on `http://localhost:3000`

## 🔑 How It Works

### Diffie-Hellman Key Exchange

1. **Key Generation**: Each user gets a unique public/private key pair
2. **Public Key Sharing**: Users can share their public keys
3. **Shared Secret**: Both users compute the same shared secret independently
4. **Secure Communication**: The shared secret can be used for encryption

### Chat Flow

1. **Login**: Enter a username to join the chat
2. **Key Generation**: Your public key is automatically generated
3. **Key Exchange**: Share your public key with other users
4. **Chat**: Send and receive messages in real-time
5. **Security**: View shared secrets for secure communication

## 📁 Project Structure

```
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/example/diffiehellmanchat/
│   │       ├── controller/          # WebSocket controllers
│   │       ├── service/             # Diffie-Hellman service
│   │       ├── model/               # Data models
│   │       └── config/              # WebSocket configuration
│   ├── src/main/resources/
│   │   └── application.properties   # Application configuration
│   └── pom.xml                      # Maven dependencies
│
├── frontend/                         # React.js Frontend
│   ├── src/
│   │   ├── components/              # React components
│   │   ├── App.js                   # Main application
│   │   └── index.js                 # Entry point
│   ├── public/
│   │   └── index.html               # HTML template
│   └── package.json                 # Node.js dependencies
│
└── README.md                         # This file
```

## 🛠️ API Endpoints

### WebSocket Endpoints
- `/ws` - WebSocket connection endpoint
- `/app/chat.sendMessage` - Send chat message
- `/app/chat.addUser` - Add user to chat
- `/app/chat.exchangeKey` - Exchange Diffie-Hellman keys

### REST Endpoints
- `/` - Home page
- `/health` - Health check

### Message Destinations
- `/topic/public` - Public chat messages
- `/user/queue/keys` - Private key exchange messages

## 🔒 Security Features

- **Diffie-Hellman Key Exchange**: Prevents man-in-the-middle attacks
- **Secure Random Generation**: Uses Java's SecureRandom for key generation
- **Standard Parameters**: Implements RFC 3526 parameters for security
- **Private Key Protection**: Private keys never leave the server

## 🎨 UI Features

- **Glassmorphism Design**: Modern, translucent UI elements
- **Responsive Layout**: Works on all device sizes
- **Real-time Updates**: Instant message delivery
- **Visual Feedback**: Connection status and key information
- **Smooth Animations**: Hover effects and transitions

## 🧪 Testing

### Backend Testing
```bash
cd backend
mvn test
```

### Frontend Testing
```bash
cd frontend
npm test
```

## 🚀 Deployment

### Backend Deployment
1. Build the JAR file: `mvn clean package`
2. Run the JAR: `java -jar target/diffie-hellman-chat-1.0.0.jar`

### Frontend Deployment
1. Build the production version: `npm run build`
2. Deploy the `build` folder to your web server

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React team for the amazing frontend library
- The cryptographic community for Diffie-Hellman algorithm

## 📞 Support

If you have any questions or issues, please open an issue on GitHub or contact the development team.

---

**Happy Secure Chatting! 🔐💬**
