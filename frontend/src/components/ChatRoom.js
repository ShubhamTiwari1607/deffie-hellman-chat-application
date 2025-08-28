import React, { useState, useEffect, useRef } from 'react';
import './ChatRoom.css';

const ChatRoom = ({ username }) => {
  const [messages, setMessages] = useState([]);
  const [messageInput, setMessageInput] = useState('');
  const [socket, setSocket] = useState(null);
  const [connected, setConnected] = useState(false);
  const [publicKey, setPublicKey] = useState('');
  const [sharedSecret, setSharedSecret] = useState('');
  const [otherUserKey, setOtherUserKey] = useState('');
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    const sock = new WebSocket('ws://localhost:8080/ws');
    setSocket(sock);

    sock.onopen = () => {
      setConnected(true);
      
      // Send join message
      sock.send(JSON.stringify({
        destination: '/app/chat.addUser',
        payload: {
          sender: username,
          type: 'JOIN'
        }
      }));
    };

    sock.onmessage = (event) => {
      try {
        console.log('Received WebSocket message:', event.data);
        const data = JSON.parse(event.data);
        console.log('Parsed message data:', data);
        
        if (data.destination === '/topic/public') {
          setMessages(prev => [...prev, data.payload]);
        } else if (data.destination === '/user/queue/keys') {
          const keyMessage = data.payload;
          console.log('Processing key message:', keyMessage);
          
          if (keyMessage.content.startsWith('Public Key:')) {
            setPublicKey(keyMessage.content.split(':')[1].trim());
          } else if (keyMessage.content.startsWith('Shared Secret:')) {
            setSharedSecret(keyMessage.content.split(':')[1].trim());
            console.log('Shared secret set:', keyMessage.content.split(':')[1].trim());
          }
        }
      } catch (error) {
        console.error('Error parsing message:', error);
      }
    };

    sock.onclose = () => {
      setConnected(false);
    };

    return () => {
      if (sock) {
        sock.close();
      }
    };
  }, [username]);

  const sendMessage = () => {
    if (messageInput.trim() && socket) {
      const chatMessage = {
        sender: username,
        content: messageInput.trim(),
        type: 'CHAT'
      };
      socket.send(JSON.stringify({
        destination: '/app/chat.sendMessage',
        payload: chatMessage
      }));
      setMessageInput('');
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      sendMessage();
    }
  };

  const exchangeKey = () => {
    if (otherUserKey.trim() && socket) {
      console.log('Attempting key exchange with:', otherUserKey.trim());
      
      const keyMessage = {
        sender: username,
        content: `Public Key: ${otherUserKey.trim()}`,
        type: 'KEY_EXCHANGE'
      };
      
      const message = {
        destination: '/app/chat.exchangeKey',
        payload: keyMessage
      };
      
      console.log('Sending message:', message);
      socket.send(JSON.stringify(message));
    } else {
      console.log('Cannot exchange keys:', { 
        hasKey: !!otherUserKey.trim(), 
        hasSocket: !!socket,
        otherUserKey: otherUserKey.trim()
      });
    }
  };

  return (
    <div className="chat-room">
      <div className="chat-header">
        <h2>Welcome, {username}!</h2>
        <div className="connection-status">
          Status: {connected ? '🟢 Connected' : '🔴 Disconnected'}
        </div>
      </div>

      <div className="key-exchange-section">
        <div className="key-info">
          <h3>🔑 Your Public Key:</h3>
          <div className="key-display">
            {publicKey ? (
              <div>
                <code>{publicKey.substring(0, 32)}...</code>
                <button 
                  onClick={() => navigator.clipboard.writeText(publicKey)}
                  className="copy-btn"
                  title="Copy full key"
                >
                  📋 Copy Full Key
                </button>
              </div>
            ) : (
              <span>Generating...</span>
            )}
          </div>
        </div>

        <div className="key-exchange">
          <h3>🔐 Key Exchange:</h3>
          <div className="key-input-group">
            <input
              type="text"
              placeholder="Enter other user's public key"
              value={otherUserKey}
              onChange={(e) => setOtherUserKey(e.target.value)}
            />
            <button onClick={exchangeKey} className="exchange-btn">
              Exchange Keys
            </button>
          </div>
                     {sharedSecret && (
             <div className="shared-secret">
               <h4>🤝 Shared Secret:</h4>
               <div>
                 <code>{sharedSecret.substring(0, 32)}...</code>
                 <button 
                   onClick={() => navigator.clipboard.writeText(sharedSecret)}
                   className="copy-btn"
                   title="Copy full secret"
                 >
                   📋 Copy Full Secret
                 </button>
               </div>
             </div>
           )}
        </div>
      </div>

      <div className="messages-container">
        <div className="messages">
          {messages.map((message, index) => (
            <div key={index} className={`message ${message.type.toLowerCase()}`}>
              <div className="message-header">
                <span className="sender">{message.sender}</span>
                <span className="timestamp">
                  {new Date(message.timestamp).toLocaleTimeString()}
                </span>
              </div>
              <div className="message-content">{message.content}</div>
            </div>
          ))}
          <div ref={messagesEndRef} />
        </div>
      </div>

      <div className="message-input">
        <input
          type="text"
          value={messageInput}
          onChange={(e) => setMessageInput(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type your message..."
          disabled={!connected}
        />
        <button onClick={sendMessage} disabled={!connected || !messageInput.trim()}>
          Send
        </button>
      </div>
    </div>
  );
};

export default ChatRoom;
