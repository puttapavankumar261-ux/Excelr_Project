package com.emp.manag.user.entity;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_experience")
public class UserExperienceEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "experience_id", nullable = false, updatable = false)
	private Integer experienceId;

	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "job_title")
	private String jobTitle;
	
	@Column(name = "start_date")
	private String startDate; // Consider using LocalDate for better date handling

	@Column(name = "end_date")
	private String endDate;   // Consider using LocalDate for better date handling
	
	@Column(name = "currently_working")
	private String currentlyWorking;

	@Column(name = "roles_and_responsibilities")
	private String rolesAndResponsibilities;

	@Column(name = "location")
	private String location;

	@Column(name = "salary")
	private BigDecimal salary;

	@Column(name = "workplace")
	private String workplace; // e.g., "On-site", "Remote", "Hybrid"

	@Column(name = "reason_for_leaving")
	private String reasonForLeaving;
	
	@Column(name = "skills")
	private String skills;
	
	@Column(name = "tools_used")
	private String toolsUsed;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user; // Foreign key to UserEntity, consider using @ManyToOne relationship
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
