package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    private String id;
    
    private String title;
    private String slug; // lowercase-course-name
    private String description;
    private String thumbnail;
    
    private String level; // BEGINNER, INTERMEDIATE, ADVANCED
    private List<String> topics; // Topic of the course
    
    // Content
    private List<Module> modules;
    
    // Statistics
    private Integer enrolledCount;
    private Integer completedCount;
    private Double averageRating; // 0-5
    private List<Review> reviews;
    
    // Status
    private Boolean isPublished;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    
    // Creator info
    private String createdBy; // instructor/admin ID
    
    // Prerequisites
    private List<String> prerequisites; // courseIds
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Module {
    private String id;
    private String title;
    private String description;
    private Integer sequenceNumber;
    private List<Lesson> lessons;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Lesson {
    private String id;
    private String title;
    private String description;
    private String contentType; // VIDEO, TEXT, INTERACTIVE
    private String contentUrl;
    private String contentText;
    private Integer duration; // minutes
    private List<String> resourceLinks;
    private Integer sequenceNumber;
    private String status; // PUBLISHED, DRAFT
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Review {
    private String userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
