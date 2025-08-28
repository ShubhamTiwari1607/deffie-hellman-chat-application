package com.example.diffiehellmanchat.handler;

import com.example.diffiehellmanchat.model.ChatMessage;
import com.example.diffiehellmanchat.service.DiffieHellmanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final DiffieHellmanService diffieHellmanService;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> sessionUsers = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(DiffieHellmanService diffieHellmanService) {
        this.diffieHellmanService = diffieHellmanService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            String payload = message.getPayload();
            System.out.println("Received WebSocket message: " + payload);
            
            Map<String, Object> data = objectMapper.readValue(payload, Map.class);
            System.out.println("Parsed message data: " + data);
            
            String destination = (String) data.get("destination");
            Map<String, Object> payloadData = (Map<String, Object>) data.get("payload");
            
            System.out.println("Destination: " + destination);
            System.out.println("Payload data: " + payloadData);
            
            if ("/app/chat.addUser".equals(destination)) {
                System.out.println("Handling addUser");
                handleAddUser(session, payloadData);
            } else if ("/app/chat.sendMessage".equals(destination)) {
                System.out.println("Handling sendMessage");
                handleSendMessage(session, payloadData);
            } else if ("/app/chat.exchangeKey".equals(destination)) {
                System.out.println("Handling exchangeKey");
                handleExchangeKey(session, payloadData);
            } else {
                System.out.println("Unknown destination: " + destination);
            }
        } catch (Exception e) {
            System.err.println("Error handling WebSocket message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAddUser(WebSocketSession session, Map<String, Object> payload) throws IOException {
        String username = (String) payload.get("sender");
        String sessionId = session.getId();
        
        sessionUsers.put(sessionId, username);
        
        // Generate Diffie-Hellman key pair for the user
        DiffieHellmanService.DiffieHellmanKeyPair keyPair = 
            diffieHellmanService.generateKeyPair(username);
        
        // Send key to the user
        ChatMessage keyMessage = new ChatMessage();
        keyMessage.setId(UUID.randomUUID().toString());
        keyMessage.setType(ChatMessage.MessageType.KEY_EXCHANGE);
        keyMessage.setContent("Public Key: " + keyPair.getPublicKey().toString(16));
        keyMessage.setSender(username);
        
        sendToUser(session, "/user/queue/keys", keyMessage);
        
        // Send join message to all users
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setId(UUID.randomUUID().toString());
        joinMessage.setType(ChatMessage.MessageType.JOIN);
        joinMessage.setContent(username + " joined the chat");
        joinMessage.setSender(username);
        
        broadcastToAll("/topic/public", joinMessage);
    }

    private void handleSendMessage(WebSocketSession session, Map<String, Object> payload) throws IOException {
        String username = sessionUsers.get(session.getId());
        if (username != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(UUID.randomUUID().toString());
            chatMessage.setSender(username);
            chatMessage.setContent((String) payload.get("content"));
            chatMessage.setType(ChatMessage.MessageType.CHAT);
            
            broadcastToAll("/topic/public", chatMessage);
        }
    }

    private void handleExchangeKey(WebSocketSession session, Map<String, Object> payload) throws IOException {
        System.out.println("handleExchangeKey called with payload: " + payload);
        
        String username = sessionUsers.get(session.getId());
        System.out.println("Username from session: " + username);
        
        if (username != null) {
            String content = (String) payload.get("content");
            System.out.println("Content: " + content);
            
            String[] parts = content.split(":");
            System.out.println("Parts length: " + parts.length);
            System.out.println("Parts: " + java.util.Arrays.toString(parts));
            
            if (parts.length == 2) {
                try {
                    String keyString = parts[1].trim();
                    System.out.println("Attempting to parse key: " + keyString);
                    
                    BigInteger otherPublicKey = new BigInteger(keyString, 16);
                    System.out.println("Parsed public key: " + otherPublicKey.toString(16));
                    
                    BigInteger sharedSecret = diffieHellmanService.computeSharedSecret(username, otherPublicKey);
                    System.out.println("Computed shared secret: " + sharedSecret.toString(16));
                    
                    // Send shared secret back to the user
                    ChatMessage secretMessage = new ChatMessage();
                    secretMessage.setId(UUID.randomUUID().toString());
                    secretMessage.setType(ChatMessage.MessageType.KEY_EXCHANGE);
                    secretMessage.setContent("Shared Secret: " + sharedSecret.toString(16));
                    secretMessage.setSender("System");
                    
                    System.out.println("Sending shared secret message: " + secretMessage.getContent());
                    sendToUser(session, "/user/queue/keys", secretMessage);
                } catch (NumberFormatException e) {
                    System.err.println("NumberFormatException: " + e.getMessage());
                    System.err.println("Invalid key format. Key must be a valid hexadecimal string without any special characters.");
                    System.err.println("Received key: " + parts[1].trim());
                    e.printStackTrace();
                    
                    // Send error message back to user
                    ChatMessage errorMessage = new ChatMessage();
                    errorMessage.setId(UUID.randomUUID().toString());
                    errorMessage.setType(ChatMessage.MessageType.KEY_EXCHANGE);
                    errorMessage.setContent("Error: Invalid key format. Please copy the full key without any special characters.");
                    errorMessage.setSender("System");
                    
                    sendToUser(session, "/user/queue/keys", errorMessage);
                } catch (Exception e) {
                    System.err.println("Unexpected error: " + e.getMessage());
                    e.printStackTrace();
                    
                    // Send error message back to user
                    ChatMessage errorMessage = new ChatMessage();
                    errorMessage.setId(UUID.randomUUID().toString());
                    errorMessage.setType(ChatMessage.MessageType.KEY_EXCHANGE);
                    errorMessage.setContent("Error: " + e.getMessage());
                    errorMessage.setSender("System");
                    
                    sendToUser(session, "/user/queue/keys", errorMessage);
                }
            } else {
                System.err.println("Invalid content format. Expected 'Public Key: <key>' but got: " + content);
            }
        } else {
            System.err.println("No username found for session: " + session.getId());
        }
    }

    private void sendToUser(WebSocketSession session, String destination, ChatMessage message) throws IOException {
        Map<String, Object> response = Map.of(
            "destination", destination,
            "payload", message
        );
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    private void broadcastToAll(String destination, ChatMessage message) throws IOException {
        Map<String, Object> response = Map.of(
            "destination", destination,
            "payload", message
        );
        String responseJson = objectMapper.writeValueAsString(response);
        
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(responseJson));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        String username = sessionUsers.get(sessionId);
        
        if (username != null) {
            // Send leave message
            ChatMessage leaveMessage = new ChatMessage();
            leaveMessage.setId(UUID.randomUUID().toString());
            leaveMessage.setType(ChatMessage.MessageType.LEAVE);
            leaveMessage.setContent(username + " left the chat");
            leaveMessage.setSender(username);
            
            try {
                broadcastToAll("/topic/public", leaveMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Clean up user data
            diffieHellmanService.removeUserKeys(username);
            sessionUsers.remove(sessionId);
        }
        
        sessions.remove(sessionId);
    }
}
