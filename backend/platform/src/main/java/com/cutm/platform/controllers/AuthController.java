package com.cutm.platform.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cutm.platform.dto.AuthRequest;
import com.cutm.platform.dto.AuthResponse;
import com.cutm.platform.dto.RegisterAdminRequest;
import com.cutm.platform.dto.RegisterRequest;
import com.cutm.platform.dto.UserDTO;
import com.cutm.platform.services.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage() != null ? e.getMessage() : "Registration failed"));
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterAdminRequest request) {
        try {
            AuthResponse response = authService.registerAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage() != null ? e.getMessage() : "Admin registration failed"));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage() != null ? e.getMessage() : "Invalid credentials"));
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        UserDTO profile = authService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }
    
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(Authentication authentication, @RequestBody UserDTO userDTO) {
        String userId = authentication.getPrincipal().toString();
        authService.updateUserProfile(userId, userDTO);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(Authentication authentication, 
                                              @RequestParam String oldPassword,
                                              @RequestParam String newPassword) {
        String userId = authentication.getPrincipal().toString();
        authService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        // Implementation for email verification
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        // Implementation for forgot password
        return ResponseEntity.ok().build();
    }
}
