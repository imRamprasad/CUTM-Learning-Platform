package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "user_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress {
    
    @Id
    private String id;
    
    private String userId;
    private String problemId;
    
    private String status; // NOT_STARTED, ATTEMPTED, SOLVED
    private Integer attempts;
    private Integer points;
    
    // Last submission info
    private String lastSubmissionId;
    private LocalDateTime lastSubmissionTime;
    
    // First solved info
    private LocalDateTime firstSolvedAt;
    
    // Time spent
    private Long totalTimeSpent; // milliseconds
    
    // Languages used
    private String languageLastUsed;
    
    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
