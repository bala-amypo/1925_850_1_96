






// package com.example.demo.controller;

// import com.example.demo.model.UserAccount;
// import com.example.demo.repository.UserAccountRepository;
// import com.example.demo.security.JwtTokenProvider;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.web.bind.annotation.*;
// import java.util.Map;

// @RestController
// @RequestMapping("/auth")
// @CrossOrigin(origins = "*") // Fixes "Failed to fetch" CORS errors
// public class AuthController {

//     private final UserAccountRepository repo;
//     private final BCryptPasswordEncoder passwordEncoder;
//     private final JwtTokenProvider tokenProvider;

//     public AuthController(UserAccountRepository repo, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
//         this.repo = repo;
//         this.passwordEncoder = passwordEncoder;
//         this.tokenProvider = tokenProvider;
//     }

//     @PostMapping("/register")
//     public ResponseEntity<?> register(@RequestBody UserAccount user) {
//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         repo.save(user);
//         return ResponseEntity.ok(Map.of("message", "User registered successfully"));
//     }

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
//         UserAccount user = repo.findByEmail(body.get("email"))
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         if (passwordEncoder.matches(body.get("password"), user.getPassword())) {
//             String token = tokenProvider.generateToken(user);
//             return ResponseEntity.ok(Map.of("accessToken", token));
//         }
//         return ResponseEntity.status(401).body("Invalid credentials");
//     }
// }





package com.example.demo.controller;

import com.example.demo.model.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") 
public class AuthController {

    private final UserAccountRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserAccountRepository repo, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    // UPDATED: Changed from Map to LoginRequest class
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        UserAccount user = repo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = tokenProvider.generateToken(user);
            return ResponseEntity.ok(Map.of("accessToken", token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}

/**
 * Helper class added at the bottom of the same file 
 * to tell Swagger what fields to show.
 */
class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}