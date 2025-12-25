package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.model.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository repo;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(
            UserAccountRepository repo,
            PasswordEncoder encoder,
            JwtTokenProvider tokenProvider) {

        this.repo = repo;
        this.encoder = encoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse authenticate(AuthRequest request) {

        UserAccount user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        String token = tokenProvider.generateToken(user);

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        return resp;
    }
}
