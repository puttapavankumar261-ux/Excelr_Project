package com.emp.manag.employee.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.employee.entity.PerformanceEntity;

@Repository
public interface PerformanceRepo extends JpaRepository<PerformanceEntity, Integer> {

	Optional<PerformanceEntity> findByEmployeeEmployeeid(Integer employeeId);
}
