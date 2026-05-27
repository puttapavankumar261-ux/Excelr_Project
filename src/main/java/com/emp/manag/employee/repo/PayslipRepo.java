package com.emp.manag.employee.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.employee.entity.PayslipEntity;

@Repository
public interface PayslipRepo extends JpaRepository<PayslipEntity, Integer> {

	Optional<PayslipEntity> findByEmployeeEmployeeidAndYearAndMonth(Integer employeeId, Integer year, Integer month);

	List<PayslipEntity> findByEmployeeEmployeeid(Integer employeeId);

	boolean existsByEmployeeEmployeeidAndYearAndMonth(Integer employeeId, Integer year, Integer month);
}
