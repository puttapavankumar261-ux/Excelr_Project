package com.emp.manag.schedule.dto;

import lombok.Data;

@Data
public class AttendanceSummaryDTO {

    private Integer workingDays;

    private Integer presentDays;

    private Integer leaveDays;

    private Integer absentDays;

    private Integer publicHolidays;

    private Double attendancePercentage;
}