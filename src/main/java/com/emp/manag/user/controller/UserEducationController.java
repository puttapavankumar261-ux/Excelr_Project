package com.emp.manag.user.controller;

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

import com.emp.manag.user.entity.UserEducationEntity;
import com.emp.manag.user.service.UserEducationService;

@RestController
@RequestMapping("/api/employee-management")
public class UserEducationController {

	@Autowired
	private UserEducationService educationService;
	
	@PostMapping("/saveEducation")
	public UserEducationEntity saveEducation(UserEducationEntity education) {
		return educationService.saveEducation(education);
	}
	
	@GetMapping("/getEducationById/{educationId}")
	public UserEducationEntity getEducationById(@PathVariable Integer educationId) {
		return educationService.getEducationById(educationId);
	}
	
	@DeleteMapping("/deleteEducationById/{educationId}")
	public String deleteEducationById(@PathVariable Integer educationId) {
		return educationService.deleteEducationById(educationId);
	}
	
	@PutMapping("/updateEducation/{educationId}")
	public String updateEducation(@PathVariable Integer educationId, @RequestBody UserEducationEntity updatedEducation) {
		return educationService.updateEducation(educationId, updatedEducation);
	}
	
	@GetMapping("/getAllEducations")
	public List<UserEducationEntity> getAllEducations() {
		return educationService.getAllEducations();
	}
	
	@DeleteMapping("/deleteAllEducations")
	public String deleteAllEducations() {
		return educationService.deleteAllEducations();
	}
}
