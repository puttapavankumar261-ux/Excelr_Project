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

import com.emp.manag.schedule.entity.AttendanceEntity;
import com.emp.manag.schedule.service.AttendanceService;

@RestController
@RequestMapping("/api/employee-management")
public class AttendanceController {

	@Autowired
	public AttendanceService service;

	@PostMapping("/saveattendance")
	public AttendanceEntity saveAttendance(@RequestBody AttendanceEntity attendance) {
		return service.saveAttendance(attendance);
	}

	@PutMapping("/updateattendance/{attendanceId}")
	public String updateAttendance(@PathVariable Integer attendanceId, @RequestBody AttendanceEntity attendance) {
		return service.updateAttendance(attendanceId, attendance);
	}

	@GetMapping("/getattendance/{attendanceId}")
	public AttendanceEntity getAttendanceById(@PathVariable Integer attendanceId) {
		return service.getAttendanceById(attendanceId);
	}

	@GetMapping("/getemployeeattendance/{employeeId}/{year}/{month}")
	public List<AttendanceEntity> getEmployeeMonthlyAttendance(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return service.getEmployeeMonthlyAttendance(employeeId, year, month);
	}

	@GetMapping("/monthlyworkingdays/{employeeId}/{year}/{month}")
	public Integer calculateMonthlyWorkingDays(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return service.calculateMonthlyWorkingDays(employeeId, year, month);
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
