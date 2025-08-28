package com.example.diffiehellmanchat.service;

import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class DiffieHellmanService {
    
    // Standard Diffie-Hellman parameters (RFC 3526)
    private static final BigInteger P = new BigInteger(
        "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74" +
        "020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F1437" +
        "4FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED" +
        "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF05" +
        "98DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB" +
        "9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B" +
        "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF695581718" +
        "3995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);
    
    private static final BigInteger G = BigInteger.valueOf(2);
    
    private final SecureRandom random = new SecureRandom();
    private final Map<String, BigInteger> userPrivateKeys = new HashMap<>();
    private final Map<String, BigInteger> userPublicKeys = new HashMap<>();
    
    public DiffieHellmanKeyPair generateKeyPair(String userId) {
        // Generate private key (random number between 1 and P-1)
        BigInteger privateKey = new BigInteger(P.bitLength() - 1, random);
        privateKey = privateKey.add(BigInteger.ONE);
        
        // Calculate public key: g^private mod p
        BigInteger publicKey = G.modPow(privateKey, P);
        
        // Store keys
        userPrivateKeys.put(userId, privateKey);
        userPublicKeys.put(userId, publicKey);
        
        return new DiffieHellmanKeyPair(publicKey, privateKey);
    }
    
    public BigInteger getPublicKey(String userId) {
        return userPublicKeys.get(userId);
    }
    
    public BigInteger computeSharedSecret(String userId, BigInteger otherPublicKey) {
        BigInteger privateKey = userPrivateKeys.get(userId);
        if (privateKey == null) {
            throw new IllegalStateException("User " + userId + " has no private key");
        }
        
        // Calculate shared secret: (otherPublicKey)^privateKey mod P
        return otherPublicKey.modPow(privateKey, P);
    }
    
    public void removeUserKeys(String userId) {
        userPrivateKeys.remove(userId);
        userPublicKeys.remove(userId);
    }
    
    public static class DiffieHellmanKeyPair {
        private final BigInteger publicKey;
        private final BigInteger privateKey;
        
        public DiffieHellmanKeyPair(BigInteger publicKey, BigInteger privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
        
        public BigInteger getPublicKey() {
            return publicKey;
        }
        
        public BigInteger getPrivateKey() {
            return privateKey;
        }
    }
}
