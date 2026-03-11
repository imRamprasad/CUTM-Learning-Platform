# Starter Code Examples - Backend (Spring Boot Java)

> Complete starter code for core backend modules

---

## Table of Contents

1. [User Model](#user-model)
2. [User Repository](#user-repository)
3. [Authentication Service](#authentication-service)
4. [Auth Controller](#auth-controller)
5. [Problem Model](#problem-model)
6. [Problem Controller](#problem-controller)
7. [Common DTOs](#common-dtos)
8. [Exception Handling](#exception-handling)
9. [Security Config](#security-config)

---

## User Model

**File**: `src/main/java/com/cutm/platform/auth/model/User.java`

```java
package com.cutm.platform.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;
    
    @Indexed(unique = true)
    private String username;
    
    private String password; // Will be bcrypt hashed
    
    private String firstName;
    private String lastName;
    private String profileImage;
    private String bio;
    
    @Builder.Default
    private String role = "STUDENT"; // STUDENT, ADMIN, INSTRUCTOR
    
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, SUSPENDED
    
    @Builder.Default
    private boolean emailVerified = false;
    
    private String emailVerificationToken;
    private LocalDateTime emailVerificationTokenExpiry;
    
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;
    
    private Map<String, String> socialLinks; // github, linkedin, etc.
    private Map<String, String> socialAuthProviders; // google, github oauth IDs
    
    private LocalDateTime lastLogin;
    
    @Builder.Default
    private int loginAttempts = 0;
    
    private LocalDateTime lockUntil; // For brute force protection
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
```

---

## User Repository

**File**: `src/main/java/com/cutm/platform/auth/repository/UserRepository.java`

```java
package com.cutm.platform.auth.repository;

import com.cutm.platform.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmailVerificationToken(String token);
    
    Optional<User> findByPasswordResetToken(String token);
    
    @Query("{ 'email': ?0, 'status': 'ACTIVE' }")
    Optional<User> findActiveByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);
}
```

---

## Authentication Service

**File**: `src/main/java/com/cutm/platform/auth/service/AuthService.java`

```java
package com.cutm.platform.auth.service;

import com.cutm.platform.auth.dto.LoginRequest;
import com.cutm.platform.auth.dto.LoginResponse;
import com.cutm.platform.auth.dto.RegisterRequest;
import com.cutm.platform.auth.model.User;
import com.cutm.platform.auth.repository.UserRepository;
import com.cutm.platform.auth.util.JwtUtil;
import com.cutm.platform.common.exception.BadRequestException;
import com.cutm.platform.common.exception.UnauthorizedException;
import com.cutm.platform.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;
    
    @Value("${app.email-verification.expiration}")
    private long emailVerificationExpiration;
    
    /**
     * Register a new user
     */
    @Transactional
    public void register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        // Validate input
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        
        // Create new user
        User user = User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .role("STUDENT")
            .status("ACTIVE")
            .emailVerified(false)
            .emailVerificationToken(UUID.randomUUID().toString())
            .emailVerificationTokenExpiry(LocalDateTime.now().plusHours(24))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        userRepository.save(user);
        
        // Send verification email
        emailService.sendVerificationEmail(user);
        
        log.info("User registered successfully: {}", request.getEmail());
    }
    
    /**
     * Login user and return JWT token
     */
    public LoginResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getEmail());
        
        User user = userRepository.findActiveByEmail(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        
        // Check if account is locked (brute force protection)
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            throw new UnauthorizedException("Account locked. Try again later");
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Increment failed login attempts
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            
            if (user.getLoginAttempts() >= 5) {
                user.setLockUntil(LocalDateTime.now().plusMinutes(30));
            }
            
            userRepository.save(user);
            throw new UnauthorizedException("Invalid email or password");
        }
        
        // Reset login attempts on successful login
        user.setLoginAttempts(0);
        user.setLockUntil(null);
        user.setLastLogin(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        
        log.info("User logged in successfully: {}", request.getEmail());
        
        return LoginResponse.builder()
            .token(token)
            .refreshToken(jwtUtil.generateRefreshToken(user.getId()))
            .expiresIn(jwtExpiration)
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }
    
    /**
     * Verify email address
     */
    @Transactional
    public void verifyEmail(String token) {
        log.info("Email verification attempt with token");
        
        User user = userRepository.findByEmailVerificationToken(token)
            .orElseThrow(() -> new BadRequestException("Invalid or expired verification token"));
        
        if (user.getEmailVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token has expired");
        }
        
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        log.info("Email verified successfully: {}", user.getEmail());
    }
    
    /**
     * Send password reset email
     */
    @Transactional
    public void forgotPassword(String email) {
        log.info("Password reset requested for: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        emailService.sendPasswordResetEmail(user, resetToken);
        
        log.info("Password reset email sent to: {}", email);
    }
    
    /**
     * Reset password with token
     */
    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        log.info("Password reset attempt");
        
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("Passwords do not match");
        }
        
        User user = userRepository.findByPasswordResetToken(token)
            .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));
        
        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        log.info("Password reset successfully for: {}", user.getEmail());
    }
}
```

---

## Auth Controller

**File**: `src/main/java/com/cutm/platform/auth/controller/AuthController.java`

```java
package com.cutm.platform.auth.controller;

import com.cutm.platform.auth.dto.LoginRequest;
import com.cutm.platform.auth.dto.RegisterRequest;
import com.cutm.platform.auth.service.AuthService;
import com.cutm.platform.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * POST /api/v1/auth/register
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received for: {}", request.getEmail());
        authService.register(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("User registered successfully. Please verify your email."));
    }
    
    /**
     * POST /api/v1/auth/login
     * Login user and get JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for: {}", request.getEmail());
        var response = authService.login(request);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }
    
    /**
     * POST /api/v1/auth/verify-email
     * Verify email with token
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<?>> verifyEmail(@RequestParam String token) {
        log.info("Email verification request received");
        authService.verifyEmail(token);
        
        return ResponseEntity.ok(ApiResponse.success("Email verified successfully"));
    }
    
    /**
     * POST /api/v1/auth/forgot-password
     * Request password reset
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestParam String email) {
        log.info("Forgot password request for: {}", email);
        authService.forgotPassword(email);
        
        return ResponseEntity.ok(ApiResponse.success("Password reset email sent"));
    }
    
    /**
     * POST /api/v1/auth/reset-password
     * Reset password with token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {
        
        log.info("Password reset request");
        authService.resetPassword(token, newPassword, confirmPassword);
        
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
    }
    
    /**
     * POST /api/v1/auth/logout
     * Logout user (frontend handles token deletion)
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout() {
        log.info("User logout request");
        // Token invalidation can be handled via blacklist (if needed)
        
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
    
    /**
     * POST /api/v1/auth/refresh-token
     * Refresh JWT token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(@RequestHeader("Authorization") String token) {
        log.info("Token refresh request");
        // Extract token from "Bearer xxx"
        String jwt = token.replace("Bearer ", "");
        var response = authService.login(null); // Implement token refresh logic
        
        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed"));
    }
}
```

---

## Problem Model

**File**: `src/main/java/com/cutm/platform/problems/model/Problem.java`

```java
package com.cutm.platform.problems.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String slug;
    
    private String title;
    private String description;
    
    @Builder.Default
    private String difficulty = "MEDIUM"; // EASY, MEDIUM, HARD
    
    private String category; // Arrays, Strings, Trees, etc.
    private List<String> subcategories;
    
    private ProblemStatement problemStatement;
    private List<Example> examples;
    private List<TestCase> testCases;
    private List<String> hints;
    private List<Editorial> editorials;
    private List<String> tags;
    
    @Builder.Default
    private Statistics statistics = new Statistics();
    
    private List<String> relatedProblemIds;
    private String createdBy; // Admin user ID
    
    @Builder.Default
    private boolean isPublished = false;
    
    @Builder.Default
    private int version = 1;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Inner classes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProblemStatement {
        private String description;
        private String constraints;
        private String inputDescription;
        private String outputDescription;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Example {
        private String input;
        private String output;
        private String explanation;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TestCase {
        private String id;
        private String input;
        private String output;
        @Builder.Default
        private boolean isHidden = false;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Editorial {
        private String language;
        private String approach;
        private String timeComplexity;
        private String spaceComplexity;
        private String code;
        private String explanation;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Statistics {
        @Builder.Default
        private long totalSubmissions = 0;
        @Builder.Default
        private long acceptedSubmissions = 0;
        @Builder.Default
        private double acceptanceRate = 0.0;
        @Builder.Default
        private double averageRating = 0.0;
        @Builder.Default
        private long totalUpvotes = 0;
        @Builder.Default
        private long totalDownvotes = 0;
    }
}
```

---

## Problem Controller

**File**: `src/main/java/com/cutm/platform/problems/controller/ProblemController.java`

```java
package com.cutm.platform.problems.controller;

import com.cutm.platform.common.dto.ApiResponse;
import com.cutm.platform.common.dto.PagedResponse;
import com.cutm.platform.problems.dto.ProblemDTO;
import com.cutm.platform.problems.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class ProblemController {
    
    private final ProblemService problemService;
    
    /**
     * GET /api/v1/problems
     * Get all problems with pagination and filters
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllProblems(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        log.info("Fetching problems - difficulty: {}, category: {}", difficulty, category);
        
        var problems = problemService.getAllProblems(difficulty, category, pageable);
        return ResponseEntity.ok(ApiResponse.success(problems, "Problems fetched successfully"));
    }
    
    /**
     * GET /api/v1/problems/{id}
     * Get problem details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getProblemById(@PathVariable String id) {
        log.info("Fetching problem: {}", id);
        
        var problem = problemService.getProblemById(id);
        return ResponseEntity.ok(ApiResponse.success(problem, "Problem fetched successfully"));
    }
    
    /**
     * POST /api/v1/problems
     * Create new problem (ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createProblem(@Valid @RequestBody ProblemDTO dto) {
        log.info("Creating new problem: {}", dto.getTitle());
        
        var problem = problemService.createProblem(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(problem, "Problem created successfully"));
    }
    
    /**
     * PUT /api/v1/problems/{id}
     * Update problem (ADMIN only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateProblem(
            @PathVariable String id,
            @Valid @RequestBody ProblemDTO dto) {
        
        log.info("Updating problem: {}", id);
        
        var problem = problemService.updateProblem(id, dto);
        return ResponseEntity.ok(ApiResponse.success(problem, "Problem updated successfully"));
    }
    
    /**
     * DELETE /api/v1/problems/{id}
     * Delete problem (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteProblem(@PathVariable String id) {
        log.info("Deleting problem: {}", id);
        
        problemService.deleteProblem(id);
        return ResponseEntity.ok(ApiResponse.success("Problem deleted successfully"));
    }
    
    /**
     * GET /api/v1/problems/{id}/solutions
     * Get problem editorials
     */
    @GetMapping("/{id}/solutions")
    public ResponseEntity<ApiResponse<?>> getProblemSolutions(@PathVariable String id) {
        log.info("Fetching solutions for problem: {}", id);
        
        var solutions = problemService.getProblemSolutions(id);
        return ResponseEntity.ok(ApiResponse.success(solutions, "Solutions fetched successfully"));
    }
}
```

---

## Common DTOs

**File**: `src/main/java/com/cutm/platform/auth/dto/LoginRequest.java`

```java
package com.cutm.platform.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
}
```

**File**: `src/main/java/com/cutm/platform/auth/dto/LoginResponse.java`

```java
package com.cutm.platform.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private long expiresIn;
    private String userId;
    private String username;
    private String email;
    private String role;
}
```

**File**: `src/main/java/com/cutm/platform/auth/dto/RegisterRequest.java`

```java
package com.cutm.platform.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
    private String username;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain uppercase, lowercase, number, and special character")
    private String password;
    
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
```

---

## Exception Handling

**File**: `src/main/java/com/cutm/platform/common/exception/GlobalExceptionHandler.java`

```java
package com.cutm.platform.common.exception;

import com.cutm.platform.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        
        log.warn("Bad request exception: {}", ex.getMessage());
        
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build());
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        
        log.warn("Unauthorized exception: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build());
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        
        log.warn("User not found exception: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.warn("Validation exception: {}", errors);
        
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .validationErrors(errors)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        log.warn("Access denied exception: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("Access denied")
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected exception:", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build());
    }
}
```

**File**: `src/main/java/com/cutm/platform/common/exception/CustomExceptions.java`

```java
package com.cutm.platform.common.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

## Security Config

**File**: `src/main/java/com/cutm/platform/config/SecurityConfig.java`

```java
package com.cutm.platform.config;

import com.cutm.platform.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            // Public endpoints
            .antMatchers("/api/v1/auth/**").permitAll()
            .antMatchers("/api/v1/problems", "/api/v1/problems/*").permitAll()
            .antMatchers("/api/v1/leaderboard/**").permitAll()
            .antMatchers("/health", "/info").permitAll()
            // Admin endpoints
            .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
            // All other endpoints require authentication
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

---

## Next Steps

1. **Create DTOs** for all modules (ProblemDTO, SubmissionDTO, etc.)
2. **Implement Services** for each module (ProblemService, SubmissionService, etc.)
3. **Create Controllers** for each module
4. **Write Unit Tests** for services and controllers
5. **Setup MongoDB** connection in application.properties
6. **Configure JWT** in JwtConfig and JwtUtil
7. **Add validation** to all DTOs using annotation validators
8. **Implement pagination** using Spring Data `Pageable`
9. **Add logging** using SLF4J and Logback
10. **Setup CI/CD** pipeline with GitHub Actions

