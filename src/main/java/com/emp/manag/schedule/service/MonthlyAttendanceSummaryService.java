package com.emp.manag.schedule.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.schedule.entity.AttendanceEntity.AttendanceStatus;
import com.emp.manag.schedule.entity.LeaveEntity.ApprovalStatus;
import com.emp.manag.schedule.entity.LeaveEntity.LeaveType;
import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;
import com.emp.manag.schedule.repo.AttendanceRepo;
import com.emp.manag.schedule.repo.EmpWeekOffRepo;
import com.emp.manag.schedule.repo.LeaveRepo;
import com.emp.manag.schedule.repo.MonthlyAttendanceSummaryRepo;
import com.emp.manag.schedule.repo.PublicHolidayRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonthlyAttendanceSummaryService {

	@Autowired
	private MonthlyAttendanceSummaryRepo monthlySummaryRepo;

	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private EmpRepo empRepo;

	@Autowired
	private EmpWeekOffRepo empWeekOffRepo;

	@Autowired
	private PublicHolidayRepo publicHolidayRepo;

	@Autowired
	private LeaveRepo leaveRepo;

	@Autowired
	private AttendanceService attendanceService;

	public MonthlyAttendanceSummaryEntity generateMonthlySummary(Integer employeeId, Integer year, Integer month) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		YearMonth yearMonth = validateYearMonth(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		MonthlyAttendanceSummaryEntity summary = monthlySummaryRepo
				.findByEmployeeEmployeeidAndYearAndMonth(employeeId, year, month)
				.orElseGet(MonthlyAttendanceSummaryEntity::new);

		summary.setEmployee(employee);
		summary.setYear(year);
		summary.setMonth(month);
		summary.setTotalCalendarDays(yearMonth.lengthOfMonth());
		summary.setWorkingDays(attendanceService.calculateMonthlyWorkingDays(employeeId, year, month));
		summary.setPresentDays(attendanceRepo.countByEmployeeEmployeeidAndAttendanceDateBetweenAndAttendanceStatus(
				employeeId, startDate, endDate, AttendanceStatus.PRESENT));
		summary.setHalfDays(attendanceRepo.countByEmployeeEmployeeidAndAttendanceDateBetweenAndAttendanceStatus(
				employeeId, startDate, endDate, AttendanceStatus.HALF_DAY));
		summary.setAbsentDays(attendanceRepo.countByEmployeeEmployeeidAndAttendanceDateBetweenAndAttendanceStatus(
				employeeId, startDate, endDate, AttendanceStatus.ABSENT));
		summary.setWeekOffDays(empWeekOffRepo.countByEmployeeEmployeeidAndWeekOffDateBetween(
				employeeId, startDate, endDate));
		summary.setPublicHolidays(publicHolidayRepo.countByPublicholidayDateBetween(startDate, endDate));
		summary.setOptionalHolidays(leaveRepo.sumLeaveDaysByType(employeeId, ApprovalStatus.APPROVED,
				LeaveType.OPTIONAL_HOLIDAY, startDate, endDate));
		summary.setSickLeaveDays(leaveRepo.sumLeaveDaysByType(employeeId, ApprovalStatus.APPROVED,
				LeaveType.SICK, startDate, endDate));
		summary.setCasualLeaveDays(leaveRepo.sumLeaveDaysByType(employeeId, ApprovalStatus.APPROVED,
				LeaveType.CASUAL, startDate, endDate));
		summary.setTotalWorkMinutes(attendanceRepo.sumTotalWorkMinutes(employeeId, startDate, endDate));
		summary.setTotalOvertimeMinutes(attendanceRepo.sumOvertimeMinutes(employeeId, startDate, endDate));

		return monthlySummaryRepo.save(summary);
	}

	public MonthlyAttendanceSummaryEntity getMonthlySummary(Integer employeeId, Integer year, Integer month) {

		validateYearMonth(year, month);

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return monthlySummaryRepo.findByEmployeeEmployeeidAndYearAndMonth(employeeId, year, month)
				.orElseThrow(() -> new RuntimeException("Monthly attendance summary not found"));
	}

	public MonthlyAttendanceSummaryEntity getSummaryById(Integer summaryId) {

		if (summaryId == null) {
			throw new RuntimeException("Summary ID is required");
		}

		return monthlySummaryRepo.findById(summaryId)
				.orElseThrow(() -> new RuntimeException("Monthly attendance summary not found with ID: " + summaryId));
	}

	public List<MonthlyAttendanceSummaryEntity> getAllSummaries() {
		return monthlySummaryRepo.findAll();
	}

	public String deleteSummary(Integer summaryId) {

		MonthlyAttendanceSummaryEntity summary = getSummaryById(summaryId);
		monthlySummaryRepo.delete(summary);

		return "Monthly attendance summary deleted successfully";
	}

	private YearMonth validateYearMonth(Integer year, Integer month) {

		if (year == null || month == null) {
			throw new RuntimeException("Year and month are required");
		}

		if (month < 1 || month > 12) {
			throw new RuntimeException("Month must be between 1 and 12");
		}

		return YearMonth.of(year, month);
	}
	
	public List<MonthlyAttendanceSummaryEntity>
	getEmployeeSummaryHistory(
	        Integer employeeId) {

	    return monthlySummaryRepo
	            .findByEmployeeEmployeeid(
	                    employeeId);
	}
}
