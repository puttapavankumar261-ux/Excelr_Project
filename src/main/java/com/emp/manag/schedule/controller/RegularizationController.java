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

import com.emp.manag.schedule.entity.RegularizationEntity;
import com.emp.manag.schedule.service.RegularizationService;

@RestController
@RequestMapping("/api/employee-management")
public class RegularizationController {

	@Autowired
	public RegularizationService service;

	@PostMapping("/saveregularization")
	public RegularizationEntity saveRegularization(@RequestBody RegularizationEntity regularization) {
		return service.saveRegularization(regularization);
	}

	@PutMapping("/updateregularization/{regularizationId}")
	public RegularizationEntity updateRegularization(@PathVariable Integer regularizationId,
			@RequestBody RegularizationEntity regularization) {
		return service.updateRegularization(regularizationId, regularization);
	}

	@PutMapping("/approveregularization/{regularizationId}/{approvedByEmployeeId}")
	public RegularizationEntity approveRegularization(@PathVariable Integer regularizationId,
			@PathVariable Integer approvedByEmployeeId) {
		return service.approveRegularization(regularizationId, approvedByEmployeeId);
	}

	@PutMapping("/rejectregularization/{regularizationId}/{rejectedByEmployeeId}")
	public RegularizationEntity rejectRegularization(@PathVariable Integer regularizationId,
			@PathVariable Integer rejectedByEmployeeId, @RequestBody String rejectionReason) {
		return service.rejectRegularization(regularizationId, rejectedByEmployeeId, rejectionReason);
	}

	@DeleteMapping("/deleteregularizationbyid/{regularizationId}")
	public String deleteRegularizationById(@PathVariable Integer regularizationId) {
		return service.deleteById(regularizationId);
	}

	@GetMapping("/getRegularizationbyId{regularizationId}")
	public RegularizationEntity getById(@PathVariable Integer regularizationId) {

		return service.getById(regularizationId);

	}

	@GetMapping("/getAllRegularization")
	public List<RegularizationEntity> getAllRegularization() {
		return service.getAllRegularization();

	}
}
