package com.emp.manag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.entity.ShiftEntity;
import com.emp.manag.service.ShiftService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class ShiftController {
	
	@Autowired
	private ShiftService service;
	
	@PostMapping("/saveshift")
	public ShiftEntity saveshift(@RequestBody ShiftEntity saveshift) {
		System.out.println("Shift Data Saved");
		return service.saveshift(saveshift);
	}
	
	@PutMapping("/updateshift/{sno}")
	public ShiftEntity updateShift(@PathVariable @RequestBody Integer sno, ShiftEntity updatedShift) {
		return service.updateShift(sno, updatedShift);
	}
	
	@GetMapping("/getshift/{sno}")
	public ShiftEntity getShiftById(@PathVariable Integer sno) {
		return service.getShiftById(sno);
	}
	
	@GetMapping("/getallshifts")
	public java.util.List<ShiftEntity> getAllShifts(@PathVariable Integer sno) {
		return service.getAllShifts();
	}
	
	@DeleteMapping("/deleteshift/{sno}")
	public String deleteShift(@PathVariable Integer sno) {
		return service.deleteShift(sno);
	}
	
	@DeleteMapping("/deleteallshifts")
	public String deleteAllShifts() {
		return service.deleteAllShifts();
	}
	
}
