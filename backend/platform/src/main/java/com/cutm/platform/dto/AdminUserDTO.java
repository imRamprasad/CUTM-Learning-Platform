package com.cutm.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private String id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
    private Integer problemsSolved;
    private Integer rank;
    private Integer totalPoints;
    private String createdAt;
}