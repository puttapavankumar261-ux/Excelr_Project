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

import com.emp.manag.user.entity.UserAssessmentEntity;
import com.emp.manag.user.service.UserAssessmentService;
import com.emp.manag.config.dto.AssessmentSessionResponse;

@RestController
@RequestMapping("/api/employee-management")
public class UserAssessmentController {
	
	@Autowired
	public UserAssessmentService userassessmentService;
	
	@PostMapping("/saveuserassessment")
	public UserAssessmentEntity saveUserAssessment(@RequestBody UserAssessmentEntity userAssessment) {
		return userassessmentService.saveUserAssessment(userAssessment);
	}
	
	@PutMapping("/updateuserassessment/{userAssessmentId}")
	public String updateUserAssessment(@PathVariable Integer userAssessmentId, @RequestBody UserAssessmentEntity updatedUserAssessment) {
		return userassessmentService.updateAssesment(userAssessmentId, updatedUserAssessment);
	}
	
	@GetMapping("/getuserassessment/{userAssessmentId}")
	public UserAssessmentEntity getUserAssessmentById(@PathVariable Integer userAssessmentId) {
		return userassessmentService.getUserAssessmentById(userAssessmentId);
	}
	
	@GetMapping("/getalluserassessments")
	public List<UserAssessmentEntity> getAllUserAssessments() {
		return userassessmentService.getAllUserAssessments();
	}
	
	@DeleteMapping("/deleteuserassessmentbyid/{userAssessmentId}")
	public String deleteUserAssessmentById(@PathVariable Integer userAssessmentId) {
		userassessmentService.deleteUserAssessmentById(userAssessmentId);
		return "User Assessment record with ID " + userAssessmentId + " has been deleted.";
	}
	
	@DeleteMapping("/deletealluserassessments")
	public String deleteAllUserAssessments() {
		userassessmentService.deleteAllUserAssessments();
		return "All User Assessment records have been deleted.";
	}

	@PostMapping("/userassessment/{userAssessmentId}/start-session")
	public AssessmentSessionResponse startAssessmentSession(@PathVariable Integer userAssessmentId) {
		return userassessmentService.startAssessmentSession(userAssessmentId);
	}

	@GetMapping("/userassessment/{userAssessmentId}/session")
	public AssessmentSessionResponse getAssessmentSession(@PathVariable Integer userAssessmentId) {
		return userassessmentService.refreshAssessmentSession(userAssessmentId);
	}

	@PostMapping("/userassessment/{userAssessmentId}/submit-session")
	public AssessmentSessionResponse submitAssessmentSession(@PathVariable Integer userAssessmentId,
			@RequestBody(required = false) UserAssessmentEntity submission) {
		return userassessmentService.submitAssessmentSession(userAssessmentId, submission);
	}

}
