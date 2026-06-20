package com.emp.manag.employee.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "payslip", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "employee_id", "year", "month" })
})
public class PayslipEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payslip_id")
	private Integer payslipId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_id", nullable = false)
	private EmpEntity employee;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "payroll_id", nullable = false)
	private PayrollEntity payroll;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "monthly_summary_id")
	private MonthlyAttendanceSummaryEntity monthlySummary;

	@Column(name = "year", nullable = false)
	private Integer year;

	@Column(name = "month", nullable = false)
	private Integer month;
	
	@Column(
	        name = "payslip_number",
	        unique = true,
	        nullable = false)
	private String payslipNumber;

	@Column(name = "pay_period_start", nullable = false)
	private LocalDate payPeriodStart;

	@Column(name = "pay_period_end", nullable = false)
	private LocalDate payPeriodEnd;

	@Column(name = "working_days")
	private Integer workingDays;

	@Column(name = "paid_days")
	private BigDecimal paidDays;

	@Column(name = "lop_days")
	private BigDecimal lopDays;

	@Column(name = "basic_salary")
	private BigDecimal basicSalary;

	@Column(name = "hra")
	private BigDecimal hra;

	@Column(name = "allowances")
	private BigDecimal allowances;

	@Column(name = "bonus")
	private BigDecimal bonus;

	@Column(name = "gross_earnings")
	private BigDecimal grossEarnings;

	@Column(name = "lop_deduction")
	private BigDecimal lopDeduction;

	@Column(name = "pf")
	private BigDecimal pf;

	@Column(name = "esi")
	private BigDecimal esi;

	@Column(name = "professional_tax")
	private BigDecimal professionalTax;

	@Column(name = "income_tax")
	private BigDecimal incomeTax;

	@Column(name = "other_deductions")
	private BigDecimal otherDeductions;

	@Column(name = "total_deductions")
	private BigDecimal totalDeductions;

	@Column(name = "net_pay")
	private BigDecimal netPay;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private PayslipStatus status;

	public enum PayslipStatus {
		GENERATED,
		HR_APPROVED,
		FINANCE_APPROVED,
		PAID,
		CANCELLED
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private EmpEntity approvedBy;

	@Column(name = "approved_on")
	private LocalDateTime approvedOn;
	
	@Column(name = "paid_on")
	private LocalDate paidOn;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
