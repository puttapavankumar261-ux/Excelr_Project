package com.emp.manag.jobboard.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "job_board")
public class JobBoardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_Board_id", nullable = false)
	private Integer jobBoardId;
	
	@OneToMany(mappedBy = "jobBoard")
	private List<AssessmentEntity> assessments;
		
	@Column(name = "job_title")
	private String jobTitle;

	@Column(name = "job_description", length = 2000)
	private String jobDescription;

	@Column(name = "job_referral")
	private String jobReferral;

	@Column(name = "location")
	private String location;

	@Column(name = "workplace")
	private String workplace; // e.g., remote, on-site, hybrid

	@Column(name = "company_name")
	private String companyname;

	@Column(name = "salary_range")
	private String salaryRange;

	@Enumerated(EnumType.STRING)
	@Column(name = "employment_type", nullable = false)
	private EmploymentType employmentType;

	public enum EmploymentType {
		FULL_TIME,
		PART_TIME,
		CONTRACT,
		INTERN,
		FREELANCER
	}

	@Column(name = "posted_date")
	private LocalDateTime postedDate;

	@Column(name = "application_deadline")
	private LocalDateTime applicationDeadline;

	@Column(name = "required_skills", length = 1000)
	private String requiredSkills;

	@Column(name = "job_category")
	private String jobCategory;

	@Column(name = "experience_level")
	private String experienceLevel;

	@Column(name = "education_requirements")
	private String educationRequirements;

	@Column(name = "job_responsibilities")
	private String jobResponsibilities;

	@Column(name = "benefits")
	private String benefits;

	@Column(name = "contact_information")
	private String contactInformation;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
