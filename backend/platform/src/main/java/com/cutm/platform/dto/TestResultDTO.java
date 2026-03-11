package com.cutm.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDTO {
    private Integer testCaseNumber;
    private String status;
    private String expectedOutput;
    private String actualOutput;
}
