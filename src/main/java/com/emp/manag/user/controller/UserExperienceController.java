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

import com.emp.manag.user.entity.UserExperienceEntity;
import com.emp.manag.user.service.UserExperienceService;

@RestController
@RequestMapping("/api/employee-management")
public class UserExperienceController {

	@Autowired
	private UserExperienceService experienceService;
	
	@PostMapping("/saveexperience")
	public UserExperienceEntity saveExperience(UserExperienceEntity experience) {
		return experienceService.saveExperience(experience);
	}

	@PutMapping("/updateexperience/{experienceId}")
	public String updateExperience(@PathVariable Integer experienceId, @RequestBody UserExperienceEntity updatedExperience) {
		return experienceService.updateExperience(experienceId, updatedExperience);
	}
	
	@GetMapping("/getexperience/{experienceId}")
	public UserExperienceEntity getExperienceById(@PathVariable Integer experienceId) {
		return experienceService.getExperienceById(experienceId);
	}
	
	@GetMapping("/getallexperience")
	public List<UserExperienceEntity> getAllExperiences() {
		return experienceService.getAllExperiences();
	}
	
	@DeleteMapping("/deleteexperience/{experienceId}")
	public String deleteExperience(@PathVariable Integer experienceId) {
		return experienceService.deleteExperience(experienceId);
	}
	
	@DeleteMapping("/deleteallExperiences/{userId}")
	public String deleteAllExperiences() {
		return experienceService.deleteAllExperiences();
	}
	
	
}

