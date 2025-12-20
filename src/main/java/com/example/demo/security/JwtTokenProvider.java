package com.example.demo.security;

import com.example.demo.model.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    
    public String generateToken(UserAccount user) {
        return "jwt-token-" + user.getEmail();
    }
    
    public boolean validateToken(String token) {
        return token != null && token.startsWith("jwt-token-");
    }
}