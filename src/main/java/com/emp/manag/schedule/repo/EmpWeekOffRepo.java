package com.emp.manag.schedule.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.EmpWeekOffEntity;

@Repository
public interface EmpWeekOffRepo extends JpaRepository<EmpWeekOffEntity, Integer> {
	
	boolean existsByEmployeeEmployeeidAndWeekOffDate(Integer employeeId, LocalDate weekOffDate);

	Optional<EmpWeekOffEntity> findByEmployeeEmployeeidAndWeekOffDate(Integer employeeId, LocalDate weekOffDate);
	
	int countByEmployeeEmployeeidAndWeekOffDateBetween(
	        Integer employeeId,
	        LocalDate startDate,
	        LocalDate endDate
	);

	List<EmpWeekOffEntity> findByEmployeeEmployeeid(Integer employeeId);

}
