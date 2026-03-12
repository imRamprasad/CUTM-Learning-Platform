package com.cutm.platform.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSubmissionDTO {
    private String id;
    private String userId;
    private String user;
    private String problemId;
    private String problem;
    private String language;
    private String verdict;
    private String submittedAt;
    private Integer plagiarismScore;
    private Boolean plagiarismDetected;
    private List<String> cheatingFlags;
    private String contestId;
    private String codeFingerprint;
}