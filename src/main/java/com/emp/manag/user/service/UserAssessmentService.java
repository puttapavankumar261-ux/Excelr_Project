package com.emp.manag.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.jobboard.entity.AssessmentEntity;
import com.emp.manag.jobboard.entity.JobApplicationEntity;
import com.emp.manag.jobboard.repo.AssessmentRepo;
import com.emp.manag.jobboard.repo.JobApplicationRepo;
import com.emp.manag.user.entity.UserAssessmentEntity;
import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.repo.UserAssessmentRepo;
import com.emp.manag.user.repo.UserRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserAssessmentService {

	@Autowired
	private UserAssessmentRepo userAssessmentRepo;

	@Autowired
	private AssessmentRepo ARepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JobApplicationRepo jobRepo;

	public UserAssessmentEntity saveUserAssessment(UserAssessmentEntity userAssessment) {

		validateUserAssessment(userAssessment);
		
		Integer userId = userAssessment.getUser().getUserId();
		Integer jobId = userAssessment.getJob().getJobId();
		Integer assessmentId = userAssessment.getAssessment().getAssessmentId();
		
		UserEntity user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
		
		JobApplicationEntity Job = jobRepo.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job application not found with ID: " + jobId));

		AssessmentEntity assessment = ARepo.findById(assessmentId)
				.orElseThrow(() -> new RuntimeException("Assessment not found with ID: " + assessmentId));
		
		userAssessment.setUser(user);
		userAssessment.setJob(Job);
		userAssessment.setAssessment(assessment);

		return userAssessmentRepo.save(userAssessment);
	}

	public String updateAssesment(Integer userAssessmentId, UserAssessmentEntity updatedUserAssessment) {

		if (userAssessmentId == null) {
			throw new RuntimeException("User Assessment ID is required");
		}

		validateUserAssessment(updatedUserAssessment);

		UserAssessmentEntity existingUserAssessment = userAssessmentRepo.findById(userAssessmentId)
				.orElseThrow(() -> new RuntimeException("User Assessment not found with ID: " + userAssessmentId));

		existingUserAssessment.setScore(updatedUserAssessment.getScore());
		existingUserAssessment.setPassed(updatedUserAssessment.getPassed());
		existingUserAssessment.setStatus(updatedUserAssessment.getStatus());
		existingUserAssessment.setStatus(updatedUserAssessment.getStatus());

		return "User assessment updated successfully";
	}

	public UserAssessmentEntity getUserAssessmentById(Integer userAssessmentId) {

		if (userAssessmentId == null) {
			throw new RuntimeException("User Assessment ID is required");
		}

		return userAssessmentRepo.findById(userAssessmentId)
				.orElseThrow(() -> new RuntimeException("User Assessment not found with ID: " + userAssessmentId));
	}

	public List<UserAssessmentEntity> getAllUserAssessments() {
		return userAssessmentRepo.findAll();
	}

	public String deleteUserAssessment(Integer userAssessmentId) {

		if (userAssessmentId == null) {
			throw new RuntimeException("User Assessment ID is required");
		}

		UserAssessmentEntity existingUserAssessment = userAssessmentRepo.findById(userAssessmentId)
				.orElseThrow(() -> new RuntimeException("User Assessment not found with ID: " + userAssessmentId));

		userAssessmentRepo.delete(existingUserAssessment);

		return "User assessment deleted successfully";
	}

	public String deleteAllUserAssessments() {
		userAssessmentRepo.deleteAll();
		return "All user assessments deleted successfully";
	}

	public void validateUserAssessment(UserAssessmentEntity userAssessment) {

		if (userAssessment == null) {
			throw new RuntimeException("User assessment details are required");
		}

		if (userAssessment.getUser() == null || userAssessment.getUser().getUserId() == null) {
			throw new RuntimeException("Valid user is required for the assessment");
		}

		if (!userRepo.existsById(userAssessment.getUser().getUserId())) {
			throw new RuntimeException("User not found with ID: " + userAssessment.getUser().getUserId());
		}

		if (userAssessment.getScore() == null) {
			throw new RuntimeException("Score is required for the assessment");
		}

		if (userAssessment.getPassed() == null) {
			throw new RuntimeException("Pass status is required for the assessment");
		}

	}

}
