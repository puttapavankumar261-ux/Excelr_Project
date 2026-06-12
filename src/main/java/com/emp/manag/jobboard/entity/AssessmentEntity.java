package com.emp.manag.jobboard.entity;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "assessment")
public class AssessmentEntity {

	@Id
	@Column(name = "assessment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer assessmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_Board_id")
	private JobBoardEntity jobBoard;

	@Column(name = "assessment_name")
	private String assessmentName;

	@Column(name = "total_score")
	private Integer totalScore;

	@Column(name = "qualifying_score")
	private Integer qualifyingScore;

	@Column(name = "assessment_stage")
	private String assessmentStage;

	@Column(name = "assessment_type")
	private String assessmentType;

	@Column(name = "assessment_result")
	private String assessmentResult;

	@Column(name = "feedback")
	private String feedback;
		
	@Column(name = "assessment_status")
	private String assessmentStatus;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "assessment", fetch = FetchType.LAZY)
	private List<ExamEntity> exams;

}
