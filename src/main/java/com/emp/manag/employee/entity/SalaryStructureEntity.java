package com.emp.manag.employee.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "salary_structure")
public class SalaryStructureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer salaryStructureId;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private EmpEntity employee;

    @Column(nullable = false)
    private BigDecimal basicSalary;

    @Column(nullable = false)
    private BigDecimal hra;

    @Column(nullable = false)
    private BigDecimal allowances;

    @Column(nullable = false)
    private BigDecimal pf;

    @Column(nullable = false)
    private BigDecimal esi;

    @Column(nullable = false)
    private BigDecimal professionalTax;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    private TaxSlabEntity taxSlab;

    @Column(nullable = false)
    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}