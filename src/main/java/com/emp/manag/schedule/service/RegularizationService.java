package com.emp.manag.schedule.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.schedule.entity.AttendanceEntity;
import com.emp.manag.schedule.entity.AttendanceEntity.AttendanceStatus;
import com.emp.manag.schedule.entity.RegularizationEntity;
import com.emp.manag.schedule.repo.AttendanceRepo;
import com.emp.manag.schedule.repo.RegularizationRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegularizationService {

	@Autowired
	private RegularizationRepo regularizationRepo;

	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private EmpRepo empRepo;

	public RegularizationEntity saveRegularization(RegularizationEntity regularization) {

		validateRegularizationRequest(regularization);

		Integer attendanceId = regularization.getAttendance().getAttendanceId();

		AttendanceEntity attendance = attendanceRepo.findById(attendanceId)
				.orElseThrow(() -> new RuntimeException("Attendance record not found with ID: " + attendanceId));

		if (regularizationRepo.existsByAttendanceAttendanceIdAndRequestedstatus(attendanceId, "PENDING")) {
			throw new RuntimeException("Pending regularization request already exists for this attendance");
		}

		regularization.setAttendance(attendance);
		regularization.setEmployee(attendance.getEmployee());
		regularization.setRequestedstatus("PENDING");
		regularization.setAttendancestatus(attendance.getAttendanceStatus().name());

		return regularizationRepo.save(regularization);
	}

	public RegularizationEntity approveRegularization(Integer regularizationId, Integer approvedByEmployeeId) {

		if (regularizationId == null) {
			throw new RuntimeException("Regularization ID is required");
		}

		if (approvedByEmployeeId == null) {
			throw new RuntimeException("Approver employee ID is required");
		}

		RegularizationEntity regularization = regularizationRepo.findById(regularizationId)
				.orElseThrow(() -> new RuntimeException("Regularization record not found with ID: " + regularizationId));

		if (!"PENDING".equalsIgnoreCase(regularization.getRequestedstatus())) {
			throw new RuntimeException("Only pending regularization requests can be approved");
		}

		EmpEntity approver = empRepo.findById(approvedByEmployeeId)
				.orElseThrow(() -> new RuntimeException("Approver not found with ID: " + approvedByEmployeeId));

		AttendanceEntity attendance = regularization.getAttendance();

		if (regularization.getRequestedCheckIn() != null) {
			attendance.setPunchInTime(regularization.getRequestedCheckIn());
		}

		if (regularization.getRequestedCheckOut() != null) {
			attendance.setPunchOutTime(regularization.getRequestedCheckOut());
		}

		attendance.setTotalWorkMinutes(calculateTotalWorkMinutes(attendance));
		attendance.setOvertimeMinutes(calculateOvertimeMinutes(attendance));
		attendance.setAttendanceStatus(calculateAttendanceStatus(attendance));

		regularization.setRequestedstatus("APPROVED");
		regularization.setApprovedby(approver);
		regularization.setApprovedOn(LocalDateTime.now());
		regularization.setRejectedOn(null);
		regularization.setAttendancestatus(attendance.getAttendanceStatus().name());

		attendanceRepo.save(attendance);

		return regularizationRepo.save(regularization);
	}

	public RegularizationEntity rejectRegularization(Integer regularizationId, Integer rejectedByEmployeeId,
			String rejectionReason) {

		if (regularizationId == null) {
			throw new RuntimeException("Regularization ID is required");
		}

		if (rejectedByEmployeeId == null) {
			throw new RuntimeException("Rejector employee ID is required");
		}

		if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
			throw new RuntimeException("Rejection reason is required");
		}

		RegularizationEntity regularization = regularizationRepo.findById(regularizationId).orElseThrow(
				() -> new RuntimeException("Regularization record not found with ID: " + regularizationId));

		if (!"PENDING".equalsIgnoreCase(regularization.getRequestedstatus())) {
			throw new RuntimeException("Only pending regularization requests can be rejected");
		}

		EmpEntity rejector = empRepo.findById(rejectedByEmployeeId)
				.orElseThrow(() -> new RuntimeException("Rejector not found with ID: " + rejectedByEmployeeId));

		regularization.setRequestedstatus("REJECTED");
		regularization.setRejectedBy(rejector);
		regularization.setRejectionReason(rejectionReason);
		regularization.setRejectedOn(LocalDateTime.now());

		return regularizationRepo.save(regularization);
	}

	public RegularizationEntity updateRegularization(Integer regularizationId, LocalTime punchInTime,
			LocalTime punchOutTime, RegularizationEntity updatedRegularization) {

		if (updatedRegularization != null) {
			if (updatedRegularization.getRequestedCheckIn() == null) {
				updatedRegularization.setRequestedCheckIn(punchInTime);
			}

			if (updatedRegularization.getRequestedCheckOut() == null) {
				updatedRegularization.setRequestedCheckOut(punchOutTime);
			}
		}

		return updateRegularization(regularizationId, updatedRegularization);
	}

	public RegularizationEntity updateRegularization(Integer regularizationId,
			RegularizationEntity updatedRegularization) {

		if (regularizationId == null) {
			throw new RuntimeException("Regularization ID is required");
		}

		validateRegularizationRequest(updatedRegularization);

		RegularizationEntity existingRegularization = regularizationRepo.findById(regularizationId)
				.orElseThrow(() -> new RuntimeException("Regularization record not found with ID: " + regularizationId));

		if (!"PENDING".equalsIgnoreCase(existingRegularization.getRequestedstatus())) {
			throw new RuntimeException("Only pending regularization requests can be updated");
		}

		existingRegularization.setRequestedCheckIn(updatedRegularization.getRequestedCheckIn());
		existingRegularization.setRequestedCheckOut(updatedRegularization.getRequestedCheckOut());
		existingRegularization.setReason(updatedRegularization.getReason());
		existingRegularization.setRemarks(updatedRegularization.getRemarks());

		return regularizationRepo.save(existingRegularization);
	}

	public String deleteById(Integer regularizationId) {

		if (regularizationId == null) {
			throw new RuntimeException("Regularization ID is required");
		}

		RegularizationEntity regularization = regularizationRepo.findById(regularizationId)
				.orElseThrow(() -> new RuntimeException("Regularization record not found"));

		if ("APPROVED".equalsIgnoreCase(regularization.getRequestedstatus())) {
			throw new RuntimeException("Approved regularization cannot be deleted");
		}

		regularizationRepo.delete(regularization);

		return "Regularization record deleted successfully.";
	}

	private void validateRegularizationRequest(RegularizationEntity regularization) {

		if (regularization == null) {
			throw new RuntimeException("Regularization details are required");
		}

		if (regularization.getAttendance() == null 
				|| regularization.getAttendance().getAttendanceId() == null) {
			throw new RuntimeException("Attendance ID is required");
		}

		if (regularization.getRequestedCheckIn() == null && regularization.getRequestedCheckOut() == null) {
			throw new RuntimeException("Requested check-in or check-out is required");
		}

		if (regularization.getRequestedCheckIn() != null && regularization.getRequestedCheckOut() != null
				&& regularization.getRequestedCheckOut().isBefore(regularization.getRequestedCheckIn())) {
			throw new RuntimeException("Requested check-out cannot be before requested check-in");
		}

		if (regularization.getReason() == null || regularization.getReason().trim().isEmpty()) {
			throw new RuntimeException("Regularization reason is required");
		}
	}

	private AttendanceStatus calculateAttendanceStatus(AttendanceEntity attendance) {

		Long totalWorkMinutes = calculateTotalWorkMinutes(attendance);

		if (totalWorkMinutes < 4 * 60) {
			return AttendanceStatus.ABSENT;
		}

		if (totalWorkMinutes < 8 * 60) {
			return AttendanceStatus.HALF_DAY;
		}

		return AttendanceStatus.PRESENT;
	}

	private Long calculateTotalWorkMinutes(AttendanceEntity attendance) {

		LocalTime punchIn = attendance.getPunchInTime();
		LocalTime punchOut = attendance.getPunchOutTime();

		if (punchIn == null || punchOut == null) {
			return 0L;
		}

		if (punchOut.isBefore(punchIn)) {
			throw new RuntimeException("Punch-out time cannot be before punch-in time");
		}

		long totalOfficeMinutes = Duration.between(punchIn, punchOut).toMinutes();
		long breakMinutes = 60;

		return Math.max(totalOfficeMinutes - breakMinutes, 0);
	}

	private Long calculateOvertimeMinutes(AttendanceEntity attendance) {

		LocalTime punchIn = attendance.getPunchInTime();
		LocalTime punchOut = attendance.getPunchOutTime();

		if (punchIn == null || punchOut == null) {
			return 0L;
		}

		if (punchOut.isBefore(punchIn)) {
			throw new RuntimeException("Punch-out time cannot be before punch-in time");
		}

		long totalOfficeMinutes = Duration.between(punchIn, punchOut).toMinutes();
		long regularOfficeMinutesIncludingBreak = 9 * 60;

		if (totalOfficeMinutes <= regularOfficeMinutesIncludingBreak) {
			return 0L;
		}

		return totalOfficeMinutes - regularOfficeMinutesIncludingBreak;
	}

	public RegularizationEntity getById(Integer regularizationId) {

	    if (regularizationId == null) {
	        throw new RuntimeException("Regularization ID is required");
	    }

	    return regularizationRepo.findById(regularizationId)
	            .orElseThrow(() ->
	                new RuntimeException(
	                    "Regularization record not found with ID: " + regularizationId
	                )
	            );
	}
	
	public List<RegularizationEntity> getAllRegularization() {
		return regularizationRepo.findAll();
	}
}
