package com.emp.manag.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "exam_pattern")
public class ExamPatternEntity {

	@Id
	@Column(name = "exam_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer examid;
	
	@Column(name = "exam_name")
	private String examName;
	
	@Column(name = "exam_type")
	private String examType;
	
	@Column(name = "exam_duration")
	private String examDuration;
	
	@Column(name = "exam_date")
	private String examDate;
	
	@Column(name = "exam_time")
	private String examtime;
	
	@Column(name = "exam_instructions")
	private String examInstructions;
	
	@Column(name = "question_pattern")
	private String questionPattern;
	
	@Column(name = "question_type")
	private String questionType;
	
	@Column(name = "answers")
	private String answers;
	
	@Column(name = "status")
	private String status;
	
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
}
