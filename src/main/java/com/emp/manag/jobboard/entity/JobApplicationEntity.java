package com.emp.manag.jobboard.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "job_applications")
public class JobApplicationEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_application_id")
    private Integer jobApplicationId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "job_id")
	private JobBoardEntity jobBoard;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private CandidateStatus status;

	public enum CandidateStatus {

		REGISTERED,

		APPLIED,

		ASSESSMENT_PENDING,
		ASSESSMENT_COMPLETED,
		ASSESSMENT_PASSED,
		ASSESSMENT_FAILED,

		INTERVIEW_SCHEDULED,
		INTERVIEW_SELECTED,
		INTERVIEW_REJECTED,

		OFFER_RELEASED,
		OFFER_ACCEPTED,

		ONBOARDED
	}

	@Column(name = "applied_date")
	private LocalDateTime appliedDate;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
