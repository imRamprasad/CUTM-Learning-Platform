package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolvedProblem {
    private String problemId;
    private String status; // ACCEPTED, WRONG_ANSWER, etc
    private Integer points;
    private Long submissionTime;
    private Integer attempts;
}
