package com.emp.manag.schedule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;
import com.emp.manag.schedule.service.MonthlyAttendanceSummaryService;

@RestController
@RequestMapping("/api/employee-management")
public class MonthlyAttendanceSummaryController {

	@Autowired
	private MonthlyAttendanceSummaryService monthlyAttendanceSummaryService;

	@PostMapping("/save-generatemonthlysummary/{employeeId}/{year}/{month}")
	public MonthlyAttendanceSummaryEntity generateMonthlySummary(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return monthlyAttendanceSummaryService.generateMonthlySummary(employeeId, year, month);
	}

	@GetMapping("/getmonthlysummary/{employeeId}/{year}/{month}")
	public MonthlyAttendanceSummaryEntity getMonthlySummary(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return monthlyAttendanceSummaryService.getMonthlySummary(employeeId, year, month);
	}

	@GetMapping("/getsummary/{summaryId}")
	public MonthlyAttendanceSummaryEntity getSummaryById(@PathVariable Integer summaryId) {
		return monthlyAttendanceSummaryService.getSummaryById(summaryId);
	}

	@GetMapping("/getallsummaries")
	public List<MonthlyAttendanceSummaryEntity> getAllSummaries() {
		return monthlyAttendanceSummaryService.getAllSummaries();
	}

	@DeleteMapping("/deletesummary/{summaryId}")
	public String deleteSummary(@PathVariable Integer summaryId) {
		return monthlyAttendanceSummaryService.deleteSummary(summaryId);
	}
}
