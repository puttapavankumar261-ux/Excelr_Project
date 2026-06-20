package com.emp.manag.employee.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.PerformanceEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.employee.repo.PerformanceRepo;
import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;
import com.emp.manag.schedule.service.MonthlyAttendanceSummaryService;

@Service
@Transactional(rollbackFor = Exception.class)
public class PerformanceService {

	@Autowired
	private PerformanceRepo performanceRepo;

	@Autowired
	private EmpRepo empRepo;

	@Autowired
	private MonthlyAttendanceSummaryService monthlySummaryService;

	public PerformanceEntity savePerformance(PerformanceEntity performance) {

		validatePerformance(performance);
		attachEmployee(performance);
		
		Integer employeeId = performance.getEmployee().getEmployeeid();	
		
		
		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		performance.setEmployee(employee);

		return performanceRepo.save(performance);
	}

	public PerformanceEntity updatePerformance(Integer performanceId, PerformanceEntity updatedPerformance) {

		if (performanceId == null) {
			throw new RuntimeException("Performance ID is required");
		}

		validatePerformance(updatedPerformance);

		PerformanceEntity existingPerformance = getPerformanceById(performanceId);
		existingPerformance.setEmployee(updatedPerformance.getEmployee());
		attachEmployee(existingPerformance);
		copyPerformanceValues(existingPerformance, updatedPerformance);

		return performanceRepo.save(existingPerformance);
	}

	public PerformanceEntity generatePerformanceFromMonthlySummary(
			Integer employeeId,
			Integer year,
	        Integer month) {

	    MonthlyAttendanceSummaryEntity summary =
	            monthlySummaryService.generateMonthlySummary(employeeId, year, month);

	    PerformanceEntity performance = performanceRepo.findByEmployeeEmployeeid(employeeId)
	                    .orElseGet(PerformanceEntity::new);

	    EmpEntity employee = empRepo.findById(employeeId)
	            .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

	    performance.setEmployee(employee);
	    performance.setTotalWorkingDays(summary.getWorkingDays());
	    performance.setTotalNumberofDaysAbsent(summary.getAbsentDays());

	    performance.setTotalNumberofDaysOnLeave(
	            safe(summary.getSickLeaveDays()) +
	            safe(summary.getCasualLeaveDays()));

	    // Total Login Hours
	    performance.setTotalLoginHrs(
	    		summary.getTotalWorkMinutes() == null ? BigDecimal.ZERO
	                    : BigDecimal.valueOf(summary.getTotalWorkMinutes())
	                            .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));

	    // Average Login Hours
	    performance.setAverageLoginTime(
	            calculateAverageLoginHours(summary));

	    performance.setTotalOvertimeHrs(  summary.getTotalOvertimeMinutes() == null
	                    ? 0
	                    : Math.toIntExact(summary.getTotalOvertimeMinutes() / 60));

	    performance.setOptionalholidays(summary.getOptionalHolidays());

	    return performanceRepo.save(performance);
	}

	public PerformanceEntity getPerformanceById(Integer performanceId) {

		if (performanceId == null) {
			throw new RuntimeException("Performance ID is required");
		}

		return performanceRepo.findById(performanceId)
				.orElseThrow(() -> new RuntimeException("Performance not found with ID: " + performanceId));
	}

	public PerformanceEntity getPerformanceByEmployee(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return performanceRepo.findByEmployeeEmployeeid(employeeId)
				.orElseThrow(() -> new RuntimeException("Performance not found for employee ID: " + employeeId));
	}

	public List<PerformanceEntity> getAllPerformances() {
		return performanceRepo.findAll();
	}

	public String deletePerformance(Integer performanceId) {

		PerformanceEntity performance = getPerformanceById(performanceId);
		performanceRepo.delete(performance);

		return "Performance deleted successfully";
	}

	private void validatePerformance(PerformanceEntity performance) {

		if (performance == null) {
			throw new RuntimeException("Performance details are required");
		}

		if (performance.getEmployee() == null || performance.getEmployee().getEmployeeid() == null) {
			throw new RuntimeException("Employee ID is required");
		}
	}

	private void attachEmployee(PerformanceEntity performance) {

		Integer employeeId = performance.getEmployee().getEmployeeid();
		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
		performance.setEmployee(employee);
	}

	private void copyPerformanceValues(PerformanceEntity target, PerformanceEntity source) {
		target.setTotalLoginHrs(source.getTotalLoginHrs());
		target.setTotalWorkingDays(source.getTotalWorkingDays());
		target.setTotalNumberofDaysAbsent(source.getTotalNumberofDaysAbsent());
		target.setTotalNumberofDaysOnLeave(source.getTotalNumberofDaysOnLeave());
		target.setAverageLoginTime(source.getAverageLoginTime());
		target.setTotalLeavebalance(source.getTotalLeavebalance());
		target.setTotalOvertimeHrs(source.getTotalOvertimeHrs());
		target.setOptionalholidays(source.getOptionalholidays());
	}

	private BigDecimal calculateAverageLoginHours(MonthlyAttendanceSummaryEntity summary) {

	    if (summary.getTotalWorkMinutes() == null
	            || summary.getPresentDays() == null
	            || summary.getPresentDays() == 0) {
	        return BigDecimal.ZERO;
	    }

	    return BigDecimal.valueOf(summary.getTotalWorkMinutes())
	            .divide(BigDecimal.valueOf(summary.getPresentDays() * 60L),
	                    2,RoundingMode.HALF_UP);
	}

	private int safe(Integer value) {
		return value == null ? 0 : value;
	}
}
