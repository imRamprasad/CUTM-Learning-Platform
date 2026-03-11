package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestStanding {
    private String rank;
    private String userId;
    private String userName;
    private Integer totalPoints;
    private Long totalTime; // time penalty in seconds
    private List<SolvedProblem> solvedProblems;
}
