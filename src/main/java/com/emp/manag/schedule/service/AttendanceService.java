package com.emp.manag.schedule.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.schedule.entity.AttendanceEntity;
import com.emp.manag.schedule.entity.AttendanceEntity.AttendanceStatus;
import com.emp.manag.schedule.entity.EmpWeekOffEntity;
import com.emp.manag.schedule.entity.LeaveEntity;
import com.emp.manag.schedule.entity.LeaveEntity.ApprovalStatus;
import com.emp.manag.schedule.entity.PublicHolidayEntity;
import com.emp.manag.schedule.entity.ShiftEntity;
import com.emp.manag.schedule.repo.AttendanceRepo;
import com.emp.manag.schedule.repo.EmpWeekOffRepo;
import com.emp.manag.schedule.repo.LeaveRepo;
import com.emp.manag.schedule.repo.PublicHolidayRepo;
import com.emp.manag.schedule.repo.ShiftRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceService {

	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private EmpRepo empRepo;

	@Autowired
	private ShiftRepo shiftRepo;

	@Autowired
	private LeaveRepo leaveRepo;

	@Autowired
	private PublicHolidayRepo publicHolidayRepo;

	@Autowired
	private EmpWeekOffRepo empWeekOffRepo;

	public AttendanceEntity saveAttendance(AttendanceEntity attendance) {

		validateAttendanceRequest(attendance, true);

		Integer employeeId = attendance.getEmployee().getEmployeeid();
		LocalDate attendanceDate = attendance.getAttendanceDate() == null ? LocalDate.now()
				: attendance.getAttendanceDate();

		if (attendanceRepo.existsByEmployeeEmployeeidAndAttendanceDate(employeeId, attendanceDate)) {
			throw new RuntimeException("Attendance already exists for this employee on " + attendanceDate);
		}

		attendance.setAttendanceDate(attendanceDate);
		attachRelations(attendance);
		recalculateAttendance(attendance);

		return attendanceRepo.save(attendance);
	}

	public String updateAttendance(Integer attendanceId, AttendanceEntity updatedAttendance) {

		if (attendanceId == null) {
			throw new RuntimeException("Attendance ID is required");
		}

		if (updatedAttendance == null) {
			throw new RuntimeException("Attendance details are required");
		}

		AttendanceEntity existingAttendance = attendanceRepo.findById(attendanceId)
				.orElseThrow(() -> new RuntimeException("Attendance record not found with ID: " + attendanceId));

		existingAttendance.setPunchInTime(updatedAttendance.getPunchInTime());
		existingAttendance.setPunchOutTime(updatedAttendance.getPunchOutTime());

		if (updatedAttendance.getShift() != null) {
			existingAttendance.setShift(updatedAttendance.getShift());
		}

		if (updatedAttendance.getLeave() != null) {
			existingAttendance.setLeave(updatedAttendance.getLeave());
		}

		if (updatedAttendance.getPublicHoliday() != null) {
			existingAttendance.setPublicHoliday(updatedAttendance.getPublicHoliday());
		}

		if (updatedAttendance.getWeekOff() != null) {
			existingAttendance.setWeekOff(updatedAttendance.getWeekOff());
		}

		validateAttendanceRequest(existingAttendance, false);
		attachRelations(existingAttendance);
		recalculateAttendance(existingAttendance);

		attendanceRepo.save(existingAttendance);

		return "Attendance record updated successfully.";
	}

	public AttendanceEntity getAttendanceById(Integer attendanceId) {

		if (attendanceId == null) {
			throw new RuntimeException("Attendance ID is required");
		}

		return attendanceRepo.findById(attendanceId)
				.orElseThrow(() -> new RuntimeException("Attendance record not found with ID: " + attendanceId));
	}

	public List<AttendanceEntity> getAllAttendances() {
		return attendanceRepo.findAll();
	}

	public List<AttendanceEntity> getEmployeeMonthlyAttendance(Integer employeeId, Integer year, Integer month) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		YearMonth yearMonth = validateYearMonth(year, month);

		return attendanceRepo.findByEmployeeEmployeeidAndAttendanceDateBetween(employeeId, yearMonth.atDay(1),
				yearMonth.atEndOfMonth());
	}

	public AttendanceEntity deleteById(Integer attendanceId) {

		AttendanceEntity existingAttendance = getAttendanceById(attendanceId);
		attendanceRepo.delete(existingAttendance);

		return existingAttendance;
	}

	public String deleteAllAttendances() {
		attendanceRepo.deleteAll();
		return "All attendance records have been deleted.";
	}

	public Integer calculateMonthlyWorkingDays(Integer employeeId, Integer year, Integer month) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		YearMonth yearMonth = validateYearMonth(year, month);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		int workingDays = 0;

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			boolean isWeekOff = empWeekOffRepo.existsByEmployeeEmployeeidAndWeekOffDate(employeeId, date);
			boolean isPublicHoliday = publicHolidayRepo.existsByPublicholidayDate(date);

			if (!isWeekOff && !isPublicHoliday) {
				workingDays++;
			}
		}

		return workingDays;
	}

	private void validateAttendanceRequest(AttendanceEntity attendance, boolean requireEmployee) {

		if (attendance == null) {
			throw new RuntimeException("Attendance details are required");
		}

		if (requireEmployee && (attendance.getEmployee() == null || attendance.getEmployee().getEmployeeid() == null)) {
			throw new RuntimeException("Employee ID is required");
		}

		if (attendance.getPunchOutTime() != null && attendance.getPunchInTime() == null) {
			throw new RuntimeException("Punch-in time is required when punch-out time is provided");
		}

		if (attendance.getPunchInTime() != null && attendance.getPunchOutTime() != null
				&& attendance.getShift() != null && !Boolean.TRUE.equals(attendance.getShift().getCrossDay())
				&& attendance.getPunchOutTime().isBefore(attendance.getPunchInTime())) {
			throw new RuntimeException("Punch-out time cannot be before punch-in time for same-day shift");
		}
	}

	private void attachRelations(AttendanceEntity attendance) {

		Integer employeeId = attendance.getEmployee().getEmployeeid();

		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
		attendance.setEmployee(employee);

		if (attendance.getShift() == null && employee.getShift() != null) {
			attendance.setShift(employee.getShift());
		}

		if (attendance.getShift() != null && attendance.getShift().getShiftid() != null) {
			Integer shiftId = attendance.getShift().getShiftid();
			ShiftEntity shift = shiftRepo.findById(shiftId)
					.orElseThrow(() -> new RuntimeException("Shift not found with ID: " + shiftId));
			attendance.setShift(shift);
		}

		if (attendance.getLeave() != null && attendance.getLeave().getLeaveId() != null) {
			Integer leaveId = attendance.getLeave().getLeaveId();
			LeaveEntity leave = leaveRepo.findById(leaveId)
					.orElseThrow(() -> new RuntimeException("Leave not found with ID: " + leaveId));

			if (leave.getApprovalStatus() != ApprovalStatus.APPROVED) {
				throw new RuntimeException("Only approved leave can be attached to attendance");
			}

			attendance.setLeave(leave);
		}

		if (attendance.getPublicHoliday() != null && attendance.getPublicHoliday().getHolidayId() != null) {
			Integer holidayId = attendance.getPublicHoliday().getHolidayId();
			PublicHolidayEntity holiday = publicHolidayRepo.findById(holidayId)
					.orElseThrow(() -> new RuntimeException("Public holiday not found with ID: " + holidayId));
			attendance.setPublicHoliday(holiday);
		} else {
			publicHolidayRepo.findByPublicholidayDate(attendance.getAttendanceDate()).ifPresent(attendance::setPublicHoliday);
		}

		if (attendance.getWeekOff() != null && attendance.getWeekOff().getWeekOffId() != null) {
			Integer weekOffId = attendance.getWeekOff().getWeekOffId();
			EmpWeekOffEntity weekOff = empWeekOffRepo.findById(weekOffId)
					.orElseThrow(() -> new RuntimeException("Week off not found with ID: " + weekOffId));
			attendance.setWeekOff(weekOff);
		} else {
			empWeekOffRepo.findByEmployeeEmployeeidAndWeekOffDate(employeeId, attendance.getAttendanceDate())
					.ifPresent(attendance::setWeekOff);
		}
	}

	private void recalculateAttendance(AttendanceEntity attendance) {
		attendance.setTotalWorkMinutes(calculateTotalWorkMinutes(attendance));
		attendance.setOvertimeMinutes(calculateOvertimeMinutes(attendance));
		attendance.setLateByMinutes(calculateLateByMinutes(attendance));
		attendance.setEarlyExitMinutes(calculateEarlyExitMinutes(attendance));
		attendance.setAttendanceStatus(calculateAttendanceStatus(attendance));
	}

	private AttendanceStatus calculateAttendanceStatus(AttendanceEntity attendance) {

		if (attendance.getPublicHoliday() != null) {
			return AttendanceStatus.HOLIDAY;
		}

		if (attendance.getWeekOff() != null) {
			return AttendanceStatus.WEEK_OFF;
		}

		if (attendance.getLeave() != null) {
			return AttendanceStatus.LEAVE;
		}

		if (attendance.getPunchInTime() == null) {
			return AttendanceStatus.ABSENT;
		}

		if (attendance.getPunchOutTime() == null) {
			return AttendanceStatus.PRESENT;
		}

		Long totalWorkMinutes = calculateTotalWorkMinutes(attendance);
		long minWorkMinutes = 8 * 60;

		if (attendance.getShift() != null && attendance.getShift().getMinWorkHours() != null) {
			minWorkMinutes = attendance.getShift().getMinWorkHours()
					.multiply(BigDecimal.valueOf(60))
					.longValue();
		}

		if (totalWorkMinutes < 4 * 60) {
			return AttendanceStatus.ABSENT;
		}

		if (totalWorkMinutes < minWorkMinutes) {
			return AttendanceStatus.HALF_DAY;
		}

		return AttendanceStatus.PRESENT;
	}

	private Long calculateTotalWorkMinutes(AttendanceEntity attendance) {

		if (attendance.getPunchInTime() == null || attendance.getPunchOutTime() == null) {
			return 0L;
		}

		long totalOfficeMinutes = calculateDurationMinutes(attendance, attendance.getPunchInTime(),
				attendance.getPunchOutTime());
		long breakMinutes = 60;

		return Math.max(totalOfficeMinutes - breakMinutes, 0);
	}

	private Long calculateOvertimeMinutes(AttendanceEntity attendance) {

		if (attendance.getPunchInTime() == null || attendance.getPunchOutTime() == null) {
			return 0L;
		}

		long totalOfficeMinutes = calculateDurationMinutes(attendance, attendance.getPunchInTime(),
				attendance.getPunchOutTime());
		long regularOfficeMinutesIncludingBreak = 9 * 60;

		return Math.max(totalOfficeMinutes - regularOfficeMinutesIncludingBreak, 0);
	}

	private Long calculateLateByMinutes(AttendanceEntity attendance) {

		if (attendance.getShift() == null || attendance.getPunchInTime() == null
				|| attendance.getShift().getStartTime() == null) {
			return 0L;
		}

		int graceMinutes = attendance.getShift().getLateGraceMinutes() == null ? 0
				: attendance.getShift().getLateGraceMinutes();
		LocalTime allowedTime = attendance.getShift().getStartTime().plusMinutes(graceMinutes);

		if (attendance.getPunchInTime().isAfter(allowedTime)) {
			return Duration.between(allowedTime, attendance.getPunchInTime()).toMinutes();
		}

		return 0L;
	}

	private Long calculateEarlyExitMinutes(AttendanceEntity attendance) {

		if (attendance.getShift() == null || attendance.getPunchOutTime() == null
				|| attendance.getShift().getEndTime() == null) {
			return 0L;
		}

		int graceMinutes = attendance.getShift().getEarlyExitGraceMinutes() == null ? 0
				: attendance.getShift().getEarlyExitGraceMinutes();
		LocalTime allowedTime = attendance.getShift().getEndTime().minusMinutes(graceMinutes);

		if (!Boolean.TRUE.equals(attendance.getShift().getCrossDay()) && attendance.getPunchOutTime().isBefore(allowedTime)) {
			return Duration.between(attendance.getPunchOutTime(), allowedTime).toMinutes();
		}

		return 0L;
	}

	private long calculateDurationMinutes(AttendanceEntity attendance, LocalTime startTime, LocalTime endTime) {

		if (endTime.isBefore(startTime)) {
			if (attendance.getShift() == null || !Boolean.TRUE.equals(attendance.getShift().getCrossDay())) {
				throw new RuntimeException("Punch-out time cannot be before punch-in time for same-day shift");
			}

			return Duration.between(startTime, endTime.plusHours(24)).toMinutes();
		}

		return Duration.between(startTime, endTime).toMinutes();
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
}
