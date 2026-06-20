package com.emp.manag.schedule.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.employee.entity.EmpEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(name = "monthly_attendance_summary" , 
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "year", "month"})
    })
public class MonthlyAttendanceSummaryEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "summary_id", unique = true, nullable = false)
    private Integer summaryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmpEntity employee;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "total_calendar_days")
    private Integer totalCalendarDays;

    @Column(name = "working_days")
    private Integer workingDays;

    @Column(name = "present_days")
    private Integer presentDays;

    @Column(name = "half_days")
    private Integer halfDays;

    @Column(name = "absent_days")
    private Integer absentDays;

    @Column(name = "week_off_days")
    private Integer weekOffDays;

    @Column(name = "public_holidays")
    private Integer publicHolidays;

    @Column(name = "optional_holidays")
    private Integer optionalHolidays;

    @Column(name = "sick_leave_days")
    private Integer sickLeaveDays;

    @Column(name = "casual_leave_days")
    private Integer casualLeaveDays;

    @Column(name = "total_work_minutes")
    private Long totalWorkMinutes;

    @Column(name = "total_overtime_minutes")
    private Long totalOvertimeMinutes;
    
    @CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
