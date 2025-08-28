import React, { useState } from 'react';
import './LoginForm.css';

const LoginForm = ({ onLogin }) => {
  const [username, setUsername] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (username.trim()) {
      onLogin(username.trim());
    }
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h2>Join the Secure Chat</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter your username"
              required
            />
          </div>
          <button type="submit" className="login-btn">
            Join Chat
          </button>
        </form>
        <div className="login-info">
          <p>🔒 Your messages will be secured using Diffie-Hellman key exchange</p>
          <p>🔑 Each user gets a unique key pair for secure communication</p>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;
