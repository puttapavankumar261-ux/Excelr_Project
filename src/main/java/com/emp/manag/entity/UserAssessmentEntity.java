package com.emp.manag.entity;

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
@Table(name = "user_assessment")
public class UserAssessmentEntity {
	
	
	@Id
	@Column(name = "assessment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer assessmentId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserEntity userId;
	
	@Column(name = "assessment_name")
	private String assessmentName;
	
	@Column(name = "assessment_date")
	private LocalDateTime assessmentDate;
	
	@Column(name = "assessment_stage")
	private String assessmentStage;
	
	@Column(name = "assessment_type")
	private String assessmentType;
	
	@Column(name = "assessment_result")
	private String assessmentResult;
	
	@Column(name = "feedback")
	private String feedback;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
