package com.emp.manag.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "attendance")
public class AttendanceEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendance_id", unique= true, nullable=false)
	private Integer attendanceId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private EmpEntity employee;

	@Column(name = "attendance_date", nullable = false)
	private LocalDate attendanceDate;

	@Column(name = "punch_in_time")
	private LocalTime punchInTime;

	@Column(name = "punch_out_time")
	private LocalTime punchOutTime;

	@Column(name = "attendance_status", nullable = false)  // PRESENT, ABSENT, LEAVE, HOLIDAY
	private String attendanceStatus;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false, insertable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	   

}
