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

@Data
@Entity
@Table(name = "exam")
public class ExamEntity {

	@Id
	@Column(name = "exam_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer examid;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assessment_id")
	private AssessmentEntity assessment;

	@OneToMany(mappedBy = "exam", fetch = FetchType.LAZY)
	private List<ExamQuestionEntity> questions;

	@Column(name = "exam_name")
	private String examName;

	@Column(name = "exam_type")
	private String examType;

	@Column(name = "duration_minutes")
	private Integer durationMinutes;

	@Column(name = "exam_instructions", columnDefinition = "TEXT")
	private String examInstructions;

	@Column(name = "total_questions")
	private Integer totalquestions;

	@Column(name = "question_pattern")
	private String questionPattern;

	@Column(name = "pass_marks")
	private Integer passMarks;

	@Column(name = "total_marks")
	private Integer totalMarks;

	@Column(name = "status")
	private String status;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
