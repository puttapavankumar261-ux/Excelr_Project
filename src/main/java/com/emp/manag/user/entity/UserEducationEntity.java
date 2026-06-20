package com.emp.manag.user.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "user_education")
@Data
public class UserEducationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "education_id")
	private Integer educationId;
	
	@Column(name = "education_type")
	private String educationType;
	
	@Column(name = "education_level")
	private String educationLevel;
	
	@Column(name = "course")
	private String course;
	
	@Column(name = "specialization")
	private String specialization;
	
	@Column(name = "university")
	private String university;
	
	@Column(name = "year_of_passing")
	private LocalDate yearOfPassing;
	
	@Column(name = "percentage")
	private BigDecimal percentage;
	
	@Column(name = "grade")
	private String grade;
	
	@Column(name = "learning_mode")
	private String learningMode; //full-time, part-time, distance learning  e-learning(online)
	
	@Column(name = "location")
	private String location;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
