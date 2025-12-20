package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.model.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    public AuthServiceImpl(UserAccountRepository userRepo,
                           BCryptPasswordEncoder encoder,
                           JwtTokenProvider jwtProvider) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {

        UserAccount user = userRepo.findByEmail(request.email)
                .orElseThrow(() ->
                        new BadRequestException("User not found"));

        if (!encoder.matches(request.password, user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        AuthResponse response = new AuthResponse();
        response.userId = user.getId();
        response.email = user.getEmail();
        response.role = user.getRole();
        response.token = jwtProvider.generateToken(user);

        return response;
    }
}
