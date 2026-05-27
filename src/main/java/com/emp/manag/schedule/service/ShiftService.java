package com.emp.manag.schedule.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.schedule.entity.ShiftEntity;
import com.emp.manag.schedule.repo.ShiftRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class ShiftService {

	@Autowired
	private ShiftRepo shiftRepo;

	public ShiftEntity saveshift(ShiftEntity shift) {

		validateShift(shift);

		if (shift.getActive() == null) {
			shift.setActive(true);
		}

		if (shift.getCrossDay() == null) {
			shift.setCrossDay(isCrossDayShift(shift.getStartTime(), shift.getEndTime()));
		}

		return shiftRepo.save(shift);
	}

	public ShiftEntity updateShift(Integer shiftId, ShiftEntity updatedShift) {

		if (shiftId == null) {
			throw new RuntimeException("Shift ID is required");
		}

		validateShift(updatedShift);

		ShiftEntity existingShift = shiftRepo.findById(shiftId)
				.orElseThrow(() -> new RuntimeException("Shift record not found with ID: " + shiftId));

		existingShift.setShiftType(updatedShift.getShiftType());
		existingShift.setShiftName(updatedShift.getShiftName());
		existingShift.setStartTime(updatedShift.getStartTime());
		existingShift.setEndTime(updatedShift.getEndTime());
		existingShift.setCrossDay(isCrossDayShift(updatedShift.getStartTime(), updatedShift.getEndTime()));
		existingShift.setLateGraceMinutes(updatedShift.getLateGraceMinutes());
		existingShift.setEarlyExitGraceMinutes(updatedShift.getEarlyExitGraceMinutes());
		existingShift.setMinWorkHours(updatedShift.getMinWorkHours());
		existingShift.setActive(updatedShift.getActive());
		existingShift.setParentShift(updatedShift.getParentShift());

		return shiftRepo.save(existingShift);
	}

	public ShiftEntity getShiftById(Integer shiftId) {

		if (shiftId == null) {
			throw new RuntimeException("Shift ID is required");
		}

		return shiftRepo.findById(shiftId)
				.orElseThrow(() -> new RuntimeException("Shift record not found with ID: " + shiftId));
	}

	public List<ShiftEntity> getAllShifts() {
		return shiftRepo.findAll();
	}

	public String deleteShift(Integer shiftId) {

		if (shiftId == null) {
			throw new RuntimeException("Shift ID is required");
		}

		ShiftEntity existingShift = shiftRepo.findById(shiftId)
				.orElseThrow(() -> new RuntimeException("Shift record not found with ID: " + shiftId));

		shiftRepo.delete(existingShift);

		return "Shift record deleted successfully";
	}

	public String deleteAllShifts() {
		shiftRepo.deleteAll();
		return "All shift records deleted successfully";
	}

	private void validateShift(ShiftEntity shift) {

		if (shift == null) {
			throw new RuntimeException("Shift details are required");
		}

		if (shift.getShiftType() == null) {
			throw new RuntimeException("Shift type is required");
		}

		if (shift.getShiftName() == null || shift.getShiftName().trim().isEmpty()) {
			throw new RuntimeException("Shift name is required");
		}

		if (shift.getStartTime() == null) {
			throw new RuntimeException("Shift start time is required");
		}

		if (shift.getEndTime() == null) {
			throw new RuntimeException("Shift end time is required");
		}

		if (shift.getStartTime().equals(shift.getEndTime())) {
			throw new RuntimeException("Shift start time and end time cannot be same");
		}

		if (shift.getLateGraceMinutes() == null || shift.getLateGraceMinutes() < 0) {
			throw new RuntimeException("Late grace minutes must be zero or greater");
		}

		if (shift.getEarlyExitGraceMinutes() != null && shift.getEarlyExitGraceMinutes() < 0) {
			throw new RuntimeException("Early exit grace minutes must be zero or greater");
		}

		if (shift.getMinWorkHours() != null && shift.getMinWorkHours().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("Minimum work hours must be greater than zero");
		}
	}

	private Boolean isCrossDayShift(LocalTime startTime, LocalTime endTime) {
		return endTime.isBefore(startTime);
	}

	public Long calculateShiftDurationMinutes(ShiftEntity shift) {

		validateShift(shift);

		LocalTime startTime = shift.getStartTime();
		LocalTime endTime = shift.getEndTime();

		if (endTime.isBefore(startTime)) {
			return Duration.between(startTime, endTime.plusHours(24)).toMinutes();
		}

		return Duration.between(startTime, endTime).toMinutes();
	}

}
