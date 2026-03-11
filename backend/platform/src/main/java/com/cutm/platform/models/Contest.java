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

@Data
@NoArgsConstructor
@AllArgsConstructor
class ProblemRef {
    private String problemId;
    private Integer points;
    private Integer sequenceNumber;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ContestStanding {
    private String rank;
    private String userId;
    private String userName;
    private Integer totalPoints;
    private Long totalTime; // time penalty in seconds
    private List<SolvedProblem> solvedProblems;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SolvedProblem {
    private String problemId;
    private String status; // ACCEPTED, WRONG_ANSWER, etc
    private Integer points;
    private Long submissionTime;
    private Integer attempts;
}
