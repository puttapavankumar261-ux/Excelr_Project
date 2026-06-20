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
@Table(name = "employee_kyc")
public class KycEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kyc_id")
	private Integer kycId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_Id", nullable = false, unique = true)
	private EmpEntity employee;

	@Column(name = "aadhaar_number", unique = true)
	private String aadhaarNumber;

	@Column(name = "pan_number", unique = true)
	private String panNumber;

	@Column(name = "bank_account_number")
	private String bankAccountNumber;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "verified", nullable = false)
	private Boolean verified;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verified_by")
	private EmpEntity verifiedBy;

	@Column(name = "verified_on")
	private LocalDateTime verifiedOn;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
