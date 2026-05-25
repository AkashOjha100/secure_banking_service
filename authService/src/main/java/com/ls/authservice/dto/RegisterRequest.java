package com.ls.authservice.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String branchId;
    private String employeeCode;
    private String email;
    private String phoneNumber;
}
