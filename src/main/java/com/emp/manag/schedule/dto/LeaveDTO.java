package com.emp.manag.schedule.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LeaveDTO {

    private Integer leaveId;

    private Integer employeeId;

    private String employeeName;

    private String department;

    private String leaveType;

    private LocalDate leaveStartDate;

    private LocalDate leaveEndDate;

    private Integer leaveDays;

    private String approvalStatus;
}