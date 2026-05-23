package com.emp.manag.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, updatable = false)
	private Integer userId;	

	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "middle_name")
	private String middleName;
	
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
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
	
	@CreationTimestamp
	@Column(name="created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
}
