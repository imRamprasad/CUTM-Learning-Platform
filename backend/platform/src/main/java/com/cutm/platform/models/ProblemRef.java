package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemRef {
    private String problemId;
    private Integer points;
    private Integer sequenceNumber;
}
