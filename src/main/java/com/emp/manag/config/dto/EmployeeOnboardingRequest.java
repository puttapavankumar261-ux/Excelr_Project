package com.emp.manag.config.dto;

import com.emp.manag.employee.entity.EmpEntity;

import lombok.Data;

@Data
public class EmployeeOnboardingRequest {

    private EmpEntity employee;

    private String username;

    private String password;

    private String role;

    private String status;
}