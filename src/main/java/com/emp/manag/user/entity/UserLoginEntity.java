package com.emp.manag.user.entity;

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

@Entity
@Data
@Table(name = "user_login")
public class UserLoginEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_login_id", nullable = false, updatable = false)
	private Integer userLoginId;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "passwordhash", nullable = false)
	private String passwordhash;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "passwordresttoken")
	private String passwordresttoken;

	@Column(name = "passwordresttokenexpiry")
	private String passwordresttokenexpiry;

	// e.g Admin/User, SUPER_ADMIN, DEPT_MANAGER, TEAM_LEAD, EMPLOYEE
	@Column(name = "role", nullable = false)
	private String role;

	@CreationTimestamp
	@Column(name = "last_Login", updatable = false)
	private LocalDateTime lastLogin;

	@Column(name = "status", nullable = false)
	private String status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
