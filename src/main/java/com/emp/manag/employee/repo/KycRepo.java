package com.emp.manag.employee.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.employee.entity.KycEntity;

@Repository
public interface KycRepo extends JpaRepository<KycEntity, Integer> {

	Optional<KycEntity> findByEmployeeEmployeeid(Integer employeeId);

	boolean existsByEmployeeEmployeeid(Integer employeeId);

	boolean existsByAadhaarNumber(String aadhaarNumber);

	boolean existsByPanNumber(String panNumber);
}
