package com.cutm.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDetailDTO {
    private String id;
    private String title;
    private String description;
    private String difficulty;
    private String problemStatement;
    private List<String> hints;
    private List<TestCaseDTO> testCases;
    private List<String> supportedLanguages;
    private List<CodeTemplateDTO> codeTemplates;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer totalSubmissions;
    private Integer totalAccepted;
    private String editorial;
}
