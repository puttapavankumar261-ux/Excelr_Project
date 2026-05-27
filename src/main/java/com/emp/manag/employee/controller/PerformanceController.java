package com.emp.manag.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.employee.entity.PerformanceEntity;
import com.emp.manag.employee.service.PerformanceService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class PerformanceController {

	@Autowired
	private PerformanceService performanceService;

	@PostMapping("/saveperformance")
	public PerformanceEntity savePerformance(@RequestBody PerformanceEntity performance) {
		return performanceService.savePerformance(performance);
	}

	@PostMapping("/generateperformance/{employeeId}/{year}/{month}")
	public PerformanceEntity generatePerformance(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return performanceService.generatePerformanceFromMonthlySummary(employeeId, year, month);
	}

	@PutMapping("/updateperformance/{performanceId}")
	public PerformanceEntity updatePerformance(@PathVariable Integer performanceId,
			@RequestBody PerformanceEntity performance) {
		return performanceService.updatePerformance(performanceId, performance);
	}

	@GetMapping("/getperformance/{performanceId}")
	public PerformanceEntity getPerformanceById(@PathVariable Integer performanceId) {
		return performanceService.getPerformanceById(performanceId);
	}

	@GetMapping("/getemployeeperformance/{employeeId}")
	public PerformanceEntity getPerformanceByEmployee(@PathVariable Integer employeeId) {
		return performanceService.getPerformanceByEmployee(employeeId);
	}

	@GetMapping("/getallperformances")
	public List<PerformanceEntity> getAllPerformances() {
		return performanceService.getAllPerformances();
	}

	@DeleteMapping("/deleteperformance/{performanceId}")
	public String deletePerformance(@PathVariable Integer performanceId) {
		return performanceService.deletePerformance(performanceId);
	}
}
