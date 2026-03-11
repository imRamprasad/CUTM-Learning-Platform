package com.cutm.platform.controllers;

import com.cutm.platform.dto.*;
import com.cutm.platform.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null));
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
