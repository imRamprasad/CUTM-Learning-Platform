package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "contests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    
    @Id
    private String id;
    
    private String title;
    private String slug;
    private String description;
    
    // Schedule
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes; // contest duration
    
    // Problems
    private List<ProblemRef> problems;
    
    // Participants
    private List<String> participantIds;
    private Integer registrationCount;
    
    // Scoring
    private String scoringType; // ICPC, ACM, IOI_STYLE
    private Integer partialScoring; // percentage for partial AC
    
    // Leaderboard
    private List<ContestStanding> standings;
    
    // Status
    private String status; // UPCOMING, ONGOING, ENDED, ARCHIVED
    private Boolean isPublic;
    
    // Creator
    private String createdBy;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
