package com.example.demo.security;

import com.example.demo.model.UserAccount;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component // Important!
public class JwtTokenProvider {
    private String jwtSecret = "change-this-secret-key-change-this-secret-key-change";

    public JwtTokenProvider() {} // No-arg constructor for tests

    public String generateToken(UserAccount user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    // ... rest of the methods (getEmail, getRole, getUserId, validateToken) as provided before
}