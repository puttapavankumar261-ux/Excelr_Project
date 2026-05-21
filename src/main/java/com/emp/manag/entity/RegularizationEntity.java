package com.emp.manag.entity;

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


@Entity
@Table(name = "regularization")
@Data
public class RegularizationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "regularization_id")
	private Integer regularizationId;

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
	private EmpEntity approvedBy;

	@CreationTimestamp
	@Column(name = "approved_on", updatable = false)
	private LocalDateTime approvedOn;
	
	@UpdateTimestamp
	@Column(name = "updated_on", updatable = true, insertable = false)
	private LocalDateTime UpdatedOn;

	@Column(name = "rejection_reason")
	private String rejectionReason;
}
