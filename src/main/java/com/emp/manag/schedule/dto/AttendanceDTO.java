package com.emp.manag.schedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AttendanceDTO {

    private Integer attendanceId;

    private Integer employeeId;

    private String employeeName;

    private String department;

    private LocalDate attendanceDate;

    private String attendanceStatus;

    private LocalTime punchInTime;

    private LocalTime punchOutTime;
}