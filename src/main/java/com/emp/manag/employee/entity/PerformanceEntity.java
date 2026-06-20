package com.emp.manag.employee.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "performance")
public class PerformanceEntity {
	
	@Id
	@Column(name = "performance_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer performanceid;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="employee_id")
	private EmpEntity employee;
	
	@Column(name = "total_login_hrs")
	private BigDecimal totalLoginHrs;
	
	@Column(name = "total_Working_days")
	private Integer totalWorkingDays;
	
	@Column(name = "total_number_of_days_absent")
	private Integer totalNumberofDaysAbsent;
	
	@Column(name = "total_number_of_days_on_leave")
	private Integer totalNumberofDaysOnLeave;
	
	@Column(name = "average_login_time")
	private BigDecimal averageLoginTime;
	
	@Column(name = "total_leavebalance")
	private Integer totalLeavebalance;
	
	@Column(name = "total_overtime_hrs")
	private Integer totalOvertimeHrs;
	
	@Column(name = "optional_holidays")
	private Integer optionalholidays;
	
	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdon;
	
	@UpdateTimestamp
	@Column(name = "updated_on")
	private LocalDateTime updatedon;
	
}
