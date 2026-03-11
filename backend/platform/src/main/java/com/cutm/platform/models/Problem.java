package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String slug; // lowercase-problem-name
    
    private String title;
    private String description;
    
    // Difficulty levels: EASY, MEDIUM, HARD
    private String difficulty;
    
    private List<String> categories; // Arrays, Strings, DynamicProgramming
    private List<String> tags; // sorting, searching, tree, etc.
    
    // Content
    private String problemStatement;
    private String hints;
    private List<String> editorialLinks;
    
    // Test cases
    private List<TestCase> testCases;
    private List<TestCase> hiddenTestCases;
    
    // Acceptance rate and stats
    private Integer totalSubmissions;
    private Integer totalAccepted;
    private Double acceptanceRate; // percentage
    private Integer totalSolved; // number of users who solved
    
    // Supported languages and boilerplate code
    private List<String> supportedLanguages; // java, python, cpp, javascript
    private List<CodeTemplate> codeTemplates;
    
    // Problem setter and editorial
    private String createdBy; // admin user ID
    private String editorial;
    private List<String> solutions;
    
    // Time constraints
    private Integer timeLimit; // seconds
    private Integer memoryLimit; // MB
    
    // Status and visibility
    private Boolean isPublished;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
