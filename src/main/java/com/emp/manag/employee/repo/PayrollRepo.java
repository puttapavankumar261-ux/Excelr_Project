package com.emp.manag.employee.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.employee.entity.PayrollEntity;

@Repository
public interface PayrollRepo extends JpaRepository<PayrollEntity, Integer> {

	List<PayrollEntity> findByEmployeeEmployeeid(Integer employeeId);

	Optional<PayrollEntity> findTopByEmployeeEmployeeidOrderByPayrollMonthDesc(Integer employeeId);

	boolean existsByEmployeeEmployeeidAndPayrollMonth(Integer employeeId, LocalDate payrollMonth);
}
