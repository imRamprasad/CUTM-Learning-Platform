package com.cutm.platform.controllers;

import com.cutm.platform.dto.UserDTO;
import com.cutm.platform.models.User;
import com.cutm.platform.models.UserProgress;
import com.cutm.platform.repositories.UserRepository;
import com.cutm.platform.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        UserDTO user = authService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @RequestBody UserDTO userDTO,
            Authentication authentication) {
        
        String userId = authentication.getPrincipal().toString();
        authService.updateUserProfile(userId, userDTO);
        UserDTO updated = authService.getUserProfile(userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        UserDTO user = authService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        List<User> users = userRepository.findByUsernameContaining(query);
        List<UserDTO> userDTOs = users.stream()
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getEmail(),
                        u.getUsername(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getProfilePictureUrl(),
                        u.getRole(),
                        u.getProblemsSolved(),
                        u.getRank(),
                        u.getTotalPoints()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
}

