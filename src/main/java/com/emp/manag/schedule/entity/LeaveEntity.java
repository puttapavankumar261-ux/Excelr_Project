package com.emp.manag.schedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.employee.entity.EmpEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employee_leave")
public class LeaveEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leave_id")
	private Integer leaveId;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmpEntity employee;

	@Column(name = "leave_start_date", nullable = false)
	private LocalDate leaveStartDate;

	@Column(name = "leave_end_date", nullable = false)
	private LocalDate leaveEndDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "leave_type", nullable = false)
	private LeaveType leaveType;

	@Enumerated(EnumType.STRING)
	@Column(name = "approval_status", nullable = false)
	private ApprovalStatus approvalStatus;

	@Column(name = "leave_days")
	private Integer leaveDays;
	
	public enum LeaveType {
	    SICK,
	    CASUAL,
	    EARNED,
	    OPTIONAL_HOLIDAY
	}

	public enum ApprovalStatus {
	    PENDING_TEAM_LEAD,
	    TEAM_LEAD_REVIEWED,
	    PENDING_MANAGER,
	    MANAGER_REVIEWED,
	    PENDING_HR,
	    HR_REVIEWED,
	    APPROVED,
	    REJECTED,
	    CANCELLED
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private EmpEntity employeeApprover;

	@Column(name = "approved_on")
	private LocalDateTime approvedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_by")
	private EmpEntity rejectedBy;

	@Column(name = "rejected_on")
	private LocalDateTime rejectedOn;

	@Column(name = "rejection_reason")
	private String rejectionReason;

	@OneToMany(mappedBy = "leave")
	@JsonIgnore
	private List<AttendanceEntity> attendances;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
}
