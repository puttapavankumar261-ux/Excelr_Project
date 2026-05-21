package com.emp.manag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.entity.ShiftEntity;
import com.emp.manag.repo.ShiftRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class ShiftService {

	@Autowired
	private ShiftRepo Repo2;

	public ShiftEntity saveshift(ShiftEntity shift) {

		System.out.println("Employee ID: " + shift.getEmployees());
		return Repo2.save(shift);
	}

	public ShiftEntity updateShift(Integer sno, ShiftEntity updatedShift) {
		ShiftEntity existShift = Repo2.findById(sno)
				.orElseThrow(() -> new RuntimeException("Shift record not found"));

		existShift.setShiftid(updatedShift.getShiftid());
		existShift.setShiftName(updatedShift.getShiftName());
		existShift.setStartTime(updatedShift.getStartTime());
		existShift.setEndTime(updatedShift.getEndTime());
		existShift.setCrossDay(updatedShift.getCrossDay());
		existShift.setLateGraceMinutes(updatedShift.getLateGraceMinutes());
		existShift.setEarlyExitGraceMinutes(updatedShift.getEarlyExitGraceMinutes());
		existShift.setMinWorkHours(updatedShift.getMinWorkHours());
		existShift.setActive(updatedShift.getActive());

		return Repo2.save(existShift);

	}

	public ShiftEntity getShiftById(Integer sno) {
		return Repo2.findById(sno).orElseThrow(() -> new RuntimeException("Shift record not found"));
	}

	public List<ShiftEntity> getAllShifts() {
		return Repo2.findAll();
	}

	public String deleteShift(Integer sno) {
		if (!Repo2.existsById(sno)) {
			throw new RuntimeException("Shift record not found");
		}
		Repo2.deleteById(sno);
		return "Shift record deleted successfully";
	}

	public String deleteAllShifts() {
		Repo2.deleteAll();
		return "All shift records deleted successfully";
	}

}
