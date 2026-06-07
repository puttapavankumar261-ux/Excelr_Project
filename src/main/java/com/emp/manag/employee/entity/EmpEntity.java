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
import lombok.Data;

@Data
@Entity
@Table(name = "employee")
public class EmpEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_Id", nullable = false, unique = true, updatable = true)
	private Integer employeeid; 
	
	@Column(name = "employee_name")
	private String employeename;
	
	@Column(name ="employee_Code", unique = true)
	private String employeeCode; // Unique code for each employee, e.g., EMP001, EMP002

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserEntity user; // Association with UserEntity, can be null for non-employee users
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shift_id")
	private ShiftEntity shift;	

	@Column(name = "image")
	private String image;
	
	// Employee → Manager
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private EmpEntity manager;

	// Manager → Subordinates
	@OneToMany(mappedBy = "manager")	
	private List<EmpEntity> teamMembers;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private EmployeeRole role;

	public enum EmployeeRole {
		ADMIN,
		HR,
		MANAGER,
		TEAM_LEAD,
		EMPLOYEE,
		PROJECT_MANAGER,
		RECRUITMENT_LEAD,
		TEACHER_TRAINER,
		FINANCE,
		PAYROLL_ADMIN
	}

	@Column(name = "joining_date")
	private LocalDate joiningDate;

	@Column(name = "resignation_date")
	private LocalDate resignationDate;
	
	@Column(name = "work_location")
	private String workLocation;

	// Designations show Seniority, Department shows which branch employee works,
	// Role shows Functional Duty in which branch employee is working.
	
	@Enumerated(EnumType.STRING)
	@Column(name = "department", nullable = false)
	private Department department;

	public enum Department {
		HR,
		SALES,
		CUSTOMER_SERVICE,
		SOFTWARE,
		FINANCE,
		OPERATIONS,
		RECRUITMENT,
		TRAINING,
		ADMIN,
		MANAGEMENT
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "designation", nullable = false)
	private JobLevel designation;
	
	public enum JobLevel {

	    INTERN,

	    TRAINEE,

	    ASSOCIATE,

	    SENIOR_ASSOCIATE,

	    SME,
	    
	    TRAINER,

	    TEAM_LEAD,

	    ASSISTANT_MANAGER,

	    MANAGER,

	    SENIOR_MANAGER,

	    DIRECTOR,

	    VICE_PRESIDENT,

	    C_LEVEL_EXECUTIVE,

	    CEO,

	    MANAGING_DIRECTOR,

	    CHAIRMAN
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "employment_type", nullable = false)
	private EmploymentType employmentType;
	
	public enum EmploymentType {

	    FULL_TIME,

	    PART_TIME,

	    CONTRACT,

	    INTERN,

	    FREELANCER
	}

	@Enumerated(EnumType.STRING)
	private EmploymentStatus employmentStatus; // ACTIVE, EXITED, ABSCONDED, TERMINATED
	
	public enum EmploymentStatus {

	    ACTIVE,

	    NOTICE_PERIOD,

	    RESIGNED,

	    TERMINATED,

	    ABSCONDED
	}

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// columns joined with other tables//

	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<AttendanceEntity> attendance;

	@OneToOne(mappedBy = "employee", fetch = FetchType.EAGER)
	@JsonIgnore
	private EmpLoginEntity login;

	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
	@JsonIgnore
	private KycEntity kyc;

}
