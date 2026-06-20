package com.emp.manag.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.jobboard.entity.JobApplicationEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, updatable = false)
	private Integer userId;
				
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserEducationEntity> userEducation;
	
	@OneToMany(mappedBy = "user")
	private List<JobApplicationEntity> applications;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserExperienceEntity> userExperience;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserAssessmentEntity> userAssessment; // One-to-one relationship with UserAssessmentEntity
	
	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserLoginEntity userLogin; // One login record for this user
	
	@Column(name = "Name", nullable = false)
	private String name;	
	
	@Column(name = "image")
	private String image;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "place_of_birth")
	private String placeofBirth;

	@Column(name = "age")
	private Integer age;

	@Column(name = "language")
	private String language; // Preferred language for communication

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber; // Use String to accommodate + and spaces/dashes

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "father_name")
	private String fatherName;

	@Column(name = "mother_name")
	private String motherName;

	@Column(name = "current_address", nullable = false)
	private String currentaddress;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "state", nullable = false)
	private String state;

	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "pincode", nullable = false)
	private String pincode;

	@Column(name = "permanent_address", nullable = false)
	private String permanentAddress;
	
	@Column(name = "marital_status")
	private String maritalStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "user_status")
	private UserStatus status; // ACTIVE, INACTIVE, SUSPENDED, etc.
	
	public enum UserStatus {
	    ACTIVE,
	    INACTIVE,
	    SUSPENDED,
	    DELETED
	}
		
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
