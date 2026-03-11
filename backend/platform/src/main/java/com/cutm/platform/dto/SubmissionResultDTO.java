package com.cutm.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResultDTO {
    private String id;
    private String status;
    private Integer testCasesPassed;
    private Integer totalTestCases;
    private Long executionTime;
    private Long memoryUsed;
    private String output;
    private String error;
    private List<TestResultDTO> testResults;
    private Integer points;
}
