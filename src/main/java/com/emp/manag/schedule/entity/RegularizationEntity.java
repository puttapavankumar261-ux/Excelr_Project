package com.emp.manag.schedule.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "regularization")
@Data
public class RegularizationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "regularization_id")
	private Integer regularizationId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendance_id", nullable = false)
	private AttendanceEntity attendance;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_Id", nullable = false)
	private EmpEntity employee;
	
	@Column(name = "requested_check_in")
	private LocalTime requestedCheckIn;

	@Column(name = "requested_check_out")
	private LocalTime requestedCheckOut;

	@Column(name = "attendance_status", nullable = false)
	private String attendancestatus;
	// PRESENT, LEAVE

	@Column(name = "reason", nullable = false)
	private String reason;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "requested_status", nullable = false)
	private String requestedstatus;
	// PENDING, APPROVED, REJECTED

	@CreationTimestamp
	@Column(name = "requested_on", updatable = false)
	private LocalDateTime requestedOn;
	
	@ManyToOne
	@JoinColumn(name="approved_by")
	private EmpEntity approvedby;
	
	@Column(name = "approved_on")
	private LocalDateTime approvedOn;
	
	@ManyToOne
	@JoinColumn(name = "rejected_by")
	private EmpEntity rejectedBy;
	
	@Column(name = "rejected_on")
	private LocalDateTime rejectedOn;

	@Column(name = "rejection_reason")
	private String rejectionReason;
	
	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;
	
	@UpdateTimestamp
	@Column(name = "updated_on", updatable = true, insertable = false)
	private LocalDateTime UpdatedOn;


}
