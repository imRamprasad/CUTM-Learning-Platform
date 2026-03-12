package com.cutm.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAdminRequest {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String adminCode;
}