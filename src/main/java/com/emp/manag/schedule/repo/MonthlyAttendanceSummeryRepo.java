package com.emp.manag.schedule.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;

@Repository
public interface MonthlyAttendanceSummeryRepo extends JpaRepository<MonthlyAttendanceSummaryEntity, Integer> {

	Optional<MonthlyAttendanceSummaryEntity> findByEmployeeEmployeeidAndYearAndMonth(
			Integer employeeId, Integer year, Integer month);
}
