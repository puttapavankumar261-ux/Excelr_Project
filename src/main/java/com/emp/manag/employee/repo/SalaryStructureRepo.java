package com.emp.manag.employee.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.employee.entity.SalaryStructureEntity;

@Repository
public interface SalaryStructureRepo
        extends JpaRepository<SalaryStructureEntity, Integer> {

    Optional<SalaryStructureEntity>
    findByEmployeeEmployeeid(Integer employeeId);

    boolean existsByEmployeeEmployeeid(Integer employeeId);
}