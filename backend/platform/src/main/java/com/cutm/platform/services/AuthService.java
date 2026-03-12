package com.cutm.platform.services;

import com.cutm.platform.dto.*;
import com.cutm.platform.models.User;
import com.cutm.platform.repositories.UserRepository;
import com.cutm.platform.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AuthService {

    @Value("${app.admin.registration-code:CUTM-ADMIN-2026}")
    private String adminRegistrationCode;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }
        
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole("STUDENT");
        user.setStatus("ACTIVE");
        user.setEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setProblemsSolved(0);
        user.setProblemsAttempted(0);
        user.setRank(0);
        user.setTotalPoints(0);
        
        userRepository.save(user);
        
        // Generate JWT token
        String token = tokenProvider.generateToken(user.getId());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());
        
        return new AuthResponse(token, refreshToken, convertToDTO(user));
    }

    public AuthResponse registerAdmin(RegisterAdminRequest request) {
        if (request.getAdminCode() == null || !request.getAdminCode().equals(adminRegistrationCode)) {
            throw new RuntimeException("Invalid admin registration code");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");
        user.setEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setProblemsSolved(0);
        user.setProblemsAttempted(0);
        user.setRank(0);
        user.setTotalPoints(0);

        userRepository.save(user);

        String token = tokenProvider.generateToken(user.getId());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());

        return new AuthResponse(token, refreshToken, convertToDTO(user));
    }
    
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if (!user.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("User account is not active");
        }
        
        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate JWT token
        String token = tokenProvider.generateToken(user.getId());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());
        
        return new AuthResponse(token, refreshToken, convertToDTO(user));
    }
    
    public UserDTO getUserProfile(String userId) {
        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        User user = userRepository.findById(safeUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }
    
    public void updateUserProfile(String userId, UserDTO userDTO) {
        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        User user = userRepository.findById(safeUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public void changePassword(String userId, String oldPassword, String newPassword) {
        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        User user = userRepository.findById(safeUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public String validateToken(String token) {
        return tokenProvider.getUserIdFromToken(token);
    }
    
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePictureUrl(),
                user.getRole(),
                user.getProblemsSolved(),
                user.getRank(),
                user.getTotalPoints()
        );
    }
}
