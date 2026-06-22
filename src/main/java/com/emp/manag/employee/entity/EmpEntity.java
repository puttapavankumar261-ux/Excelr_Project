package com.emp.manag.employee.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.schedule.entity.AttendanceEntity;
import com.emp.manag.schedule.entity.LeaveEntity;
import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;
import com.emp.manag.schedule.entity.ShiftEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import lombok.Data;

@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
@Data
@Entity
@Table(name = "employee")
public class EmpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_Id", nullable = false, unique = true)
    private Integer employeeid;

    @Column(name = "employee_name")
    private String employeename;

    @Column(name = "employee_Code", unique = true)
    private String employeeCode;

    @Column(name = "phone_number")
    private String phonenumber;

    @Column(name = "company_email")
    private String companyemail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
    })
    private ShiftEntity shift;

    @Column(name = "image")
    private String image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private EmpEntity manager;

    @JsonIgnore
    @OneToMany(mappedBy = "manager")
    private List<EmpEntity> subordinates;

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

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "basic_salary")
    private BigDecimal basicSalary;

    @Column(name = "hra")
    private BigDecimal hra;

    @Column(name = "allowances")
    private BigDecimal allowances;

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
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

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

    /* ATTENDANCE */

    @OneToMany(
            mappedBy = "employee",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<AttendanceEntity> attendance;

    /* LOGIN */

    @OneToOne(
            mappedBy = "employee",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private EmpLoginEntity login;

    /* KYC */

    @OneToOne(
            mappedBy = "employee",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private KycEntity kyc;

    /* SALARY STRUCTURE */

    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private SalaryStructureEntity salaryStructure;

    /* LEAVES */

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<LeaveEntity> leaves;

    /* MONTHLY ATTENDANCE SUMMARY */

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<MonthlyAttendanceSummaryEntity> monthlyAttendanceSummaries;

    /* PAYROLL */

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<PayrollEntity> payrolls;
}