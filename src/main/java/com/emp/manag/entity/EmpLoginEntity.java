package com.emp.manag.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@Column(name = "password_rest_token", nullable = false)
	private String password_reset_token;
	
	@Column(name = "password_reset_expiry", nullable = false)
	private String password_reset_expiry;

	// login relationship
	@OneToOne
	@JoinColumn(name = "employee_id")
	private EmpEntity employee;

	// e.g Admin/User, SUPER_ADMIN, DEPT_MANAGER, TEAM_LEAD, EMPLOYEE
	@Column(name = "role", nullable = false)
	private String role;

	// level -1,2,3,4,5
	@Column(name = "approval_name", nullable = false)
	private EmpEntity employeeName;

	@Column(name = "approval_level", nullable = false)
	private Integer approvalLevel;

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
