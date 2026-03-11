package com.cutm.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDTO {
    private String id;
    private String title;
    private String slug;
    private String difficulty;
    private List<String> categories;
    private List<String> tags;
    private Integer totalSubmissions;
    private Integer totalAccepted;
    private Double acceptanceRate;
    private Boolean solved;
}
