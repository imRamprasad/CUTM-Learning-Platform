package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResult {
    private Integer testCaseNumber;
    private String status;
    private String expectedOutput;
    private String actualOutput;
    private Long executionTime;
}
