package com.emp.manag.setup.dto;

import lombok.Data;

@Data
public class AdminSetupRequest {

    private String employeeName;

    private String username;

    private String password;

    private String role;

    private String designation;

    private String department;
}