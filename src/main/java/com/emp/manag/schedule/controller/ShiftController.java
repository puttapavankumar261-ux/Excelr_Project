package com.emp.manag.schedule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.schedule.entity.ShiftEntity;
import com.emp.manag.schedule.service.ShiftService;

@RequestMapping("/api/employee-management")
@RestController
public class ShiftController {

	@Autowired
	private ShiftService service;

	@PostMapping("/saveshift")
	public ShiftEntity saveShift(@RequestBody ShiftEntity shift) {
		return service.saveshift(shift);
	}

	@PutMapping("/updateshift/{shiftId}")
	public ShiftEntity updateShift(@PathVariable Integer shiftId, @RequestBody ShiftEntity updatedShift) {
		return service.updateShift(shiftId, updatedShift);
	}

	@GetMapping("/getshift/{shiftId}")
	public ShiftEntity getShiftById(@PathVariable Integer shiftId) {
		return service.getShiftById(shiftId);
	}

	@GetMapping("/getallshifts")
	public List<ShiftEntity> getAllShifts() {
		return service.getAllShifts();
	}

	@DeleteMapping("/deleteshift/{shiftId}")
	public String deleteShift(@PathVariable Integer shiftId) {
		return service.deleteShift(shiftId);
	}

	@DeleteMapping("/deleteallshifts")
	public String deleteAllShifts() {
		return service.deleteAllShifts();
	}
}
