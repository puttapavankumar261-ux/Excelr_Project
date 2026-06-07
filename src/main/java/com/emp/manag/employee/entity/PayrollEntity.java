package com.emp.manag.employee.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payroll")
public class PayrollEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payrollId")
	private Integer payrollId;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private EmpEntity employee;

	@Column(name = "basic_salary", nullable = false)
	private BigDecimal basicSalary;

	@Column(name = "hra", nullable = false)
	private BigDecimal hra;

	@Column(name = "allowances", nullable = false)
	private BigDecimal allowances;

	@Column(name = "bonus", nullable = false)
	private BigDecimal bonus;

	@Column(name = "deductions", nullable = false)
	private BigDecimal deductions;

	@Column(name = "pf", nullable = false)
	private BigDecimal pf;

	@Column(name = "esi", nullable = false)
	private BigDecimal esi;

	@Column(name = "professional_tax", nullable = false)
	private BigDecimal professionalTax;

	@Column(name = "income_tax", nullable = false)
	private BigDecimal incomeTax;

	@Column(name = "gross_salary", nullable = false)
	private BigDecimal grossSalary;

	@Column(name = "net_salary", nullable = false)
	private BigDecimal netSalary;

	@Column(name = "tax_slab", nullable = false)
	private String taxSlab;

	@Column(name = "approved", nullable = false)
	private Boolean approved;

	@Column(name = "payroll_month", nullable = false)
	private LocalDate payrollMonth;

	public enum PayrollStatus {

		DRAFT,

		HR_APPROVED,

		FINANCE_APPROVED,

		PAID,

		REJECTED,
		
		On_Hold
	}

	@Enumerated(EnumType.STRING)
	private PayrollStatus status;
	
	@ManyToOne
	@JoinColumn(name = "approved_by")
	private EmpEntity approvedBy;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDate createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDate updatedAt;

}
