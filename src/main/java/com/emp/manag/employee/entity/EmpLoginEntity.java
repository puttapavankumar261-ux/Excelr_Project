package com.emp.manag.employee.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "employee_login")
public class EmpLoginEntity {

	@Id
	@Column(name = "emp_login_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer emploginid;
	
	// login relationship
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_id")
	private EmpEntity employee;
		
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "password_reset_token")
	private String passwordResetToken;

	@Column(name = "password_reset_expiry")
	private LocalDateTime passwordResetExpiry;

	// e.g Admin/User, SUPER_ADMIN, DEPT_MANAGER, TEAM_LEAD, EMPLOYEE
	@Column(name = "role", nullable = false)
	private String role;
	
	@UpdateTimestamp
	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "status", nullable = false)
	private String status; // Active/Inactive

	@Column(name = "first_login")
	private Boolean firstLogin = true;
	
	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdon;

	@UpdateTimestamp
	@Column(name = "updated_on")
	private LocalDateTime updatedon;
}
