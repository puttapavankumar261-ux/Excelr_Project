package com.emp.manag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.entity.RegularizationEntity;
import com.emp.manag.service.RegularizationService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RegularizationController {
	
	@Autowired
	public RegularizationService service;

	@PostMapping("/saveregularization")
	public String saveRegularization(@RequestBody RegularizationEntity regularization) {
		service.saveRegularization(regularization);
		return "Attendance Regularization Data Saved";
	}

	@PutMapping("/updateregularization/{sno}")
	public String updateRegularization(@PathVariable @RequestBody RegularizationEntity regularization) {
		service.updateRegularization(regularization.getRegularizationId(), regularization.getRequestedCheckIn(),
				regularization.getRequestedCheckOut(), regularization);
		return "Attendance Regularization Data Updated";
	}

	@DeleteMapping("/deleteregularizationbyid/{sno}")
	public String deleteRegularizationById(@PathVariable Integer sno) {
		service.deleteById(sno);
		return "Attendance Regularization Data Deleted by ID";
	}

}
