import React, { useState } from 'react';
import './App.css';
import ChatRoom from './components/ChatRoom';
import LoginForm from './components/LoginForm';

function App() {
  const [username, setUsername] = useState('');

  const handleLogin = (user) => {
    setUsername(user);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>🔐 Diffie-Hellman Secure Chat</h1>
        <p>End-to-end encrypted messaging using Diffie-Hellman key exchange</p>
      </header>
      
      {!username ? (
        <LoginForm onLogin={handleLogin} />
      ) : (
        <ChatRoom username={username} />
      )}
    </div>
  );
}

export default App;
