package com.cutm.platform.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;
    
    private String username;
    private String password; // BCrypt hashed
    
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
    
    private String role; // STUDENT, ADMIN
    private String status; // ACTIVE, SUSPENDED, DELETED
    
    // Account verification
    private Boolean emailVerified;
    private String emailVerificationToken;
    private LocalDateTime emailVerificationTokenExpiry;
    
    // Password reset
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;
    
    // Statistics
    private Integer problemsSolved;
    private Integer problemsAttempted;
    private Integer rank;
    private Integer totalPoints;
    
    // Skills and interests
    private List<String> skills; // Java, Python, C++, etc.
    private List<String> interests; // Algorithms, DataStructures, etc.
    
    // Social
    private List<String> followers;
    private List<String> following;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    
    // Preferences
    private String preferredLanguage; // java, python, cpp, javascript
    private String theme; // light, dark
    private Boolean notificationsEnabled;
}
