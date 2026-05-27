package com.emp.manag.employee.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.schedule.entity.AttendanceEntity;
import com.emp.manag.schedule.entity.ShiftEntity;
import com.emp.manag.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "employee")
public class EmpEntity {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_Id", nullable = false, unique = true, updatable = true)
	private Integer employeeid; // Unique code for each employee, e.g., EMP001, EMP002
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserEntity user; // Association with UserEntity, can be null for non-employee users

	// Employee → Manager
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private EmpEntity manager;

	// Manager → Subordinates
	@OneToMany(mappedBy = "manager")
	private List<EmpEntity> subordinates;

	@Column(name = "employee_name", nullable = false)
	private String employeeName;


	@Column(name = "role", nullable = false) // Project Manager, Recruitment Lead, Teacher Trainer
	private String role;

	@Column(name = "joining_date")
	private LocalDate joiningDate;

	@Column(name = "resignation_date")
	private LocalDate resignationDate;

	// Designations show Seniority, Department shows which branch employee works,
	// Role shows Functional Duty in which branch employee is working.

	@Column(name = "designation", nullable = false) // Intern\Fresher, Associate, Sr.Associate, SME, TL, Manager,
	private String designation;

	@Column(name = "department", nullable = false) // HR, Sales, Customer Service, Software...
	private String department;

	@Column(name = "employment_type") // FULL_TIME, CONTRACT, Intern, Part-Time, Work From Home
	private String employmentType;

	@Column(name = "employment_status") // ACTIVE, RESIGNED
	private String employmentStatus; // ACTIVE, EXITED, ABSCONDED, TERMINATED
	
	@Column(name = "work_location")
	private String workLocation;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// columns joined with other tables//

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shift_id")
	private ShiftEntity shift;
	
	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<AttendanceEntity> attendance;
	
	@OneToOne(mappedBy= "employee", fetch = FetchType.EAGER)
	@JsonIgnore
	private EmpLoginEntity login;

	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
	@JsonIgnore
	private KycEntity kyc;

}
