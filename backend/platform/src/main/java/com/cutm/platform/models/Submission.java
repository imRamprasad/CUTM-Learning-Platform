package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    
    @Id
    private String id;
    
    private String userId;
    private String problemId;
    private String contestId; // null if practice submission
    
    private String language; // java, python, cpp, javascript
    private String sourceCode;
    
    private String status; // ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED, COMPILATION_ERROR, RUNTIME_ERROR, NOT_JUDGED
    
    private Integer testCasesPassed;
    private Integer totalTestCases;
    
    // Execution details
    private Long executionTime; // milliseconds
    private Long memoryUsed; // bytes
    
    // Error messages
    private String compileError;
    private String runtimeError;
    private String output;
    
    // Test case details
    private List<TestCaseResult> testResults;
    
    // Scoring
    private Integer points;
    private Long submissionTime; // time taken from start
    
    // Timestamps
    private LocalDateTime submittedAt;
    private LocalDateTime judgedAt;
    
    // Metadata
    private Boolean isLatest; // latest submission for this user-problem
    private String verdict; // PASS, FAIL, etc
}
