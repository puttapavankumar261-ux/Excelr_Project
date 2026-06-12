package com.emp.manag.jobboard.controller;

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

import com.emp.manag.jobboard.entity.AssessmentEntity;
import com.emp.manag.jobboard.service.AssessmentService;

@RestController
@RequestMapping("/api/employee-management")
public class AssessmentController {

	@Autowired
	private AssessmentService assessmentService;

	@PostMapping("/SaveAssessment")
	public AssessmentEntity saveAssessment(@RequestBody AssessmentEntity assessment) {
		return assessmentService.save(assessment);
	}

	@PutMapping("/UpdateAssessment/${assessmentId}")
	public String updateAssessment(@PathVariable Integer assessmentId, @RequestBody AssessmentEntity updatedAssessment) {
		return assessmentService.updateAssessment(assessmentId, updatedAssessment);
	}

	@DeleteMapping("/DeleteAssessment/${assessmentId}")
	public String deleteAssessment(@PathVariable Integer assessmentId) {
		return assessmentService.deleteAssessment(assessmentId);
	}

	@DeleteMapping("/DeleteAllAssessments")
	public String deleteAllAssessments() {
		return assessmentService.deleteAllAssessments();
	}

	@GetMapping("/GetAssessmentById/${assessmentId}")
	public AssessmentEntity getAssessmentById(@PathVariable Integer assessmentId) {
		return assessmentService.getAssessmentById(assessmentId);
	}

	@GetMapping("/GetAllAssessments")
	public List<AssessmentEntity> getAllAssessments() {
		return assessmentService.getAllAssessments();
	}
}
