package com.emp.manag.employee.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.employee.entity.EmpLoginEntity;

@Repository
public interface EmpLoginRepo extends JpaRepository<EmpLoginEntity, Integer> {

	Optional<EmpLoginEntity> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmployeeEmployeeid(Integer employeeId);

}
