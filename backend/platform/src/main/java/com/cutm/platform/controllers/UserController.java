package com.cutm.platform.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cutm.platform.dto.AdminUserDTO;
import com.cutm.platform.dto.UserDTO;
import com.cutm.platform.models.Submission;
import com.cutm.platform.models.User;
import com.cutm.platform.repositories.SubmissionRepository;
import com.cutm.platform.repositories.UserRepository;
import com.cutm.platform.services.AuthService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final DateTimeFormatter ACTIVITY_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter USER_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy");

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

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<User> users;
        boolean hasRole = role != null && !role.isBlank();
        boolean hasStatus = status != null && !status.isBlank();
        boolean hasQuery = query != null && !query.isBlank();
        String normalizedRole = role == null ? "" : role.toUpperCase();
        String normalizedStatus = status == null ? "" : status.toUpperCase();

        if (hasQuery) {
            users = userRepository.findByEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase(query, query);
        } else if (hasRole && hasStatus) {
            users = userRepository.findByStatusAndRole(normalizedStatus, normalizedRole);
        } else if (hasRole) {
            users = userRepository.findByRole(normalizedRole);
        } else if (hasStatus) {
            users = userRepository.findByStatus(normalizedStatus);
        } else {
            users = userRepository.findAll();
        }

        int safePage = Math.max(0, page);
        int safeSize = Math.max(1, size);
        int start = safePage * safeSize;
        int end = Math.min(start + safeSize, users.size());
        if (start >= users.size()) {
            return ResponseEntity.ok(List.of());
        }

        List<AdminUserDTO> result = users.subList(start, end).stream()
                .map(this::toAdminUserDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin/{userId}")
    public ResponseEntity<AdminUserDTO> getAdminUserById(@PathVariable String userId, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        return userRepository.findById(safeUserId)
                .map(user -> ResponseEntity.ok(toAdminUserDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AdminUserDTO> createUser(@RequestBody Map<String, String> payload, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String email = payload.get("email");
        String username = payload.getOrDefault("username", email != null ? email.split("@")[0] : null);
        String password = payload.getOrDefault("password", "Temp@123");
        if (email == null || email.isBlank() || username == null || username.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (userRepository.findByEmail(email).isPresent() || userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(payload.getOrDefault("firstName", ""));
        user.setLastName(payload.getOrDefault("lastName", ""));
        user.setRole(payload.getOrDefault("role", "STUDENT").toUpperCase());
        user.setStatus(payload.getOrDefault("status", "ACTIVE").toUpperCase());
        user.setEmailVerified(false);
        user.setProblemsSolved(0);
        user.setProblemsAttempted(0);
        user.setRank(0);
        user.setTotalPoints(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toAdminUserDTO(saved));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AdminUserDTO> updateUserByAdmin(
            @PathVariable String userId,
            @RequestBody Map<String, String> payload,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        User user = userRepository.findById(safeUserId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (payload.containsKey("firstName")) user.setFirstName(payload.get("firstName"));
        if (payload.containsKey("lastName")) user.setLastName(payload.get("lastName"));
        if (payload.containsKey("username") && payload.get("username") != null && !payload.get("username").isBlank()) {
            user.setUsername(payload.get("username"));
        }
        if (payload.containsKey("role") && payload.get("role") != null) user.setRole(payload.get("role").toUpperCase());
        if (payload.containsKey("status") && payload.get("status") != null) user.setStatus(payload.get("status").toUpperCase());
        if (payload.containsKey("email") && payload.get("email") != null && !payload.get("email").isBlank()) {
            user.setEmail(payload.get("email"));
        }
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return ResponseEntity.ok(toAdminUserDTO(saved));
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<AdminUserDTO> updateUserStatus(
            @PathVariable String userId,
            @RequestBody Map<String, String> payload,
            Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        User user = userRepository.findById(safeUserId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String status = payload.getOrDefault("status", "ACTIVE").toUpperCase();
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok(toAdminUserDTO(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserByAdmin(@PathVariable String userId, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        if (!userRepository.existsById(safeUserId)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(safeUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/activity")
    public ResponseEntity<List<Map<String, String>>> getUserActivity(@PathVariable String userId, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String safeUserId = Objects.requireNonNull(userId, "userId cannot be null");
        User user = userRepository.findById(safeUserId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, String>> activity = new ArrayList<>();
        if (user.getLastLoginAt() != null) {
            activity.add(Map.of(
                    "id", "ACT-LOGIN-" + user.getId(),
                    "type", "LOGIN",
                    "description", "User logged in",
                    "at", user.getLastLoginAt().format(ACTIVITY_TIME_FORMAT)
            ));
        }

        List<Submission> recentSubmissions = submissionRepository.findTop20ByUserIdOrderBySubmittedAtDesc(safeUserId);
        for (Submission submission : recentSubmissions) {
            activity.add(Map.of(
                    "id", "ACT-SUB-" + submission.getId(),
                    "type", "SUBMISSION",
                    "description", "Submitted solution with status: " + submission.getStatus(),
                    "at", submission.getSubmittedAt() != null
                            ? submission.getSubmittedAt().format(ACTIVITY_TIME_FORMAT)
                            : LocalDateTime.now().format(ACTIVITY_TIME_FORMAT)
            ));
        }

        activity.sort(Comparator.comparing((Map<String, String> row) -> row.getOrDefault("at", "")).reversed());
        return ResponseEntity.ok(activity);
    }

    private AdminUserDTO toAdminUserDTO(User user) {
        return new AdminUserDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getStatus(),
                user.getProblemsSolved(),
                user.getRank(),
                user.getTotalPoints(),
                user.getCreatedAt() != null ? user.getCreatedAt().format(USER_DATE_FORMAT) : ""
        );
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}

