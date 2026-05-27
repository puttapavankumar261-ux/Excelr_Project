package com.emp.manag.employee.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "employee_login")
public class EmpLoginEntity {

	@Id
	@Column(name = "login_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer loginid;
	
	// login relationship
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_id")
	private EmpEntity employee;
		
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@Column(name = "password_reset_token")
	private String password_reset_token;
	
	@Column(name = "password_reset_expiry")
	private String password_reset_expiry;

	// e.g Admin/User, SUPER_ADMIN, DEPT_MANAGER, TEAM_LEAD, EMPLOYEE
	@Column(name = "role", nullable = false)
	private String role;
	
	@CreationTimestamp
	@Column(name = "last_Login", updatable = false)
	private LocalDateTime lastLogin;

	@Column(name = "status", nullable = false)
	private String Status; // Active/Inactive

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdon;

	@UpdateTimestamp
	@Column(name = "updated_on")
	private LocalDateTime updatedon;
}
