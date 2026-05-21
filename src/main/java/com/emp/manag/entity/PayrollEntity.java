package com.emp.manag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	private Integer payrollId;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private EmpEntity employee;

	@Column(name = "salary")
	private double salary;

	@Column(name = "month")
	private String month;

}
