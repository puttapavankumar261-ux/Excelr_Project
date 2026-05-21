package com.emp.manag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.entity.AttendanceEntity;
import com.emp.manag.service.AttendanceService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AttendanceController {

	@Autowired
	public AttendanceService service;

	@PostMapping("/saveattendance/{employeeSno}")
	public AttendanceEntity saveAttendance(@RequestBody AttendanceEntity attendance,
			@PathVariable Integer employeeSno) {
		return service.saveAttendance(attendance, employeeSno);
	}

	@DeleteMapping("/deleteattendancebyid/{attendanceId}")
	public String deleteAttendanceById(@PathVariable Integer attendanceId) {
		service.deleteById(attendanceId);
		return "Attendance record with ID " + attendanceId + " has been deleted.";
	}

	@GetMapping("/getallattendance")
	public List<AttendanceEntity> getAllAttendance() {
		return service.getAllAttendances();
	}

}
