package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        if ("admin".equals(request.getUsername())
                && "admin".equals(request.getPassword())) {
            return new AuthResponse("dummy-token", "SUCCESS");
        }
        throw new RuntimeException("Invalid credentials");
    }
}
