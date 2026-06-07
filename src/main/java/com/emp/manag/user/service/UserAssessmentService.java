package com.emp.manag.user.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.jobboard.entity.AssessmentEntity;
import com.emp.manag.jobboard.entity.ExamEntity;
import com.emp.manag.jobboard.entity.JobApplicationEntity;

import com.emp.manag.jobboard.repo.AssessmentRepo;
import com.emp.manag.jobboard.repo.ExamRepo;
import com.emp.manag.jobboard.repo.JobApplicationRepo;
import com.emp.manag.config.dto.AssessmentSessionResponse;
import com.emp.manag.user.entity.UserAssessmentEntity;
import com.emp.manag.user.entity.UserAssessmentEntity.AssessmentSessionStatus;
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

	@Autowired
	private ExamRepo examRepo;
	

	public UserAssessmentEntity saveUserAssessment(UserAssessmentEntity userAssessment) {

		validateUserAssessment(userAssessment);
		
		Integer userId = userAssessment.getUser().getUserId();
		Integer jobId = userAssessment.getJob().getJobApplicationId();
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
		
		
		if (userAssessment.getSessionStatus() == null) {
			userAssessment.setSessionStatus(AssessmentSessionStatus.ASSIGNED);
		}

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
		existingUserAssessment.setAssessment(updatedUserAssessment.getAssessment());
		existingUserAssessment.setUser(updatedUserAssessment.getUser());
		existingUserAssessment.setJob(updatedUserAssessment.getJob());
		userAssessmentRepo.save(existingUserAssessment);

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

	public String deleteUserAssessmentById(Integer userAssessmentId) {

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

	public AssessmentSessionResponse startAssessmentSession(Integer userAssessmentId) {
		UserAssessmentEntity userAssessment = getUserAssessmentById(userAssessmentId);
		LocalDateTime now = LocalDateTime.now();

		if (userAssessment.getSessionStatus() == AssessmentSessionStatus.SUBMITTED) {
			return buildSessionResponse(userAssessment, "Assessment already submitted");
		}
		if (userAssessment.getSessionStatus() == AssessmentSessionStatus.EXPIRED) {
			return buildSessionResponse(userAssessment, "Assessment time already expired");
		}
		if (userAssessment.getSessionStartedAt() != null) {
			return refreshAssessmentSession(userAssessmentId);
		}

		int durationMinutes = resolveDurationMinutes(userAssessment);
		userAssessment.setSessionStartedAt(now);
		userAssessment.setSessionEndsAt(now.plusMinutes(durationMinutes));
		userAssessment.setSessionStatus(AssessmentSessionStatus.IN_PROGRESS);
		userAssessmentRepo.save(userAssessment);

		return buildSessionResponse(userAssessment, "Assessment session started");
	}

	public AssessmentSessionResponse refreshAssessmentSession(Integer userAssessmentId) {
		UserAssessmentEntity userAssessment = getUserAssessmentById(userAssessmentId);
		expireIfNeeded(userAssessment);
		return buildSessionResponse(userAssessment, "Assessment session status fetched");
	}

	public AssessmentSessionResponse submitAssessmentSession(Integer userAssessmentId, UserAssessmentEntity submission) {
		UserAssessmentEntity userAssessment = getUserAssessmentById(userAssessmentId);
		expireIfNeeded(userAssessment);

		if (userAssessment.getSessionStatus() == AssessmentSessionStatus.EXPIRED) {
			throw new RuntimeException("Assessment duration is completed. Submission is closed.");
		}
		if (userAssessment.getSessionStatus() == AssessmentSessionStatus.SUBMITTED) {
			return buildSessionResponse(userAssessment, "Assessment already submitted");
		}
		if (userAssessment.getSessionStatus() != AssessmentSessionStatus.IN_PROGRESS) {
			throw new RuntimeException("Assessment session has not started");
		}

		if (submission != null) {
			userAssessment.setScore(submission.getScore());
			userAssessment.setPassed(submission.getPassed());			
		}
		userAssessment.setSubmittedAt(LocalDateTime.now());
		userAssessment.setSessionStatus(AssessmentSessionStatus.SUBMITTED);
		userAssessmentRepo.save(userAssessment);

		return buildSessionResponse(userAssessment, "Assessment submitted successfully");
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
		
	}

	private int resolveDurationMinutes(UserAssessmentEntity userAssessment) {
		Integer assessmentId = userAssessment.getAssessment().getAssessmentId();
		List<ExamEntity> exams = examRepo.findByAssessmentAssessmentId(assessmentId);
		return exams.stream()
				.map(ExamEntity::getDurationMinutes)
				.filter(duration -> duration != null && duration > 0)
				.findFirst()
				.orElse(60);
	}

	private void expireIfNeeded(UserAssessmentEntity userAssessment) {
		if (userAssessment.getSessionStatus() == AssessmentSessionStatus.IN_PROGRESS
				&& userAssessment.getSessionEndsAt() != null
				&& !LocalDateTime.now().isBefore(userAssessment.getSessionEndsAt())) {
			userAssessment.setSessionStatus(AssessmentSessionStatus.EXPIRED);
			userAssessmentRepo.save(userAssessment);
		}
	}

	private AssessmentSessionResponse buildSessionResponse(UserAssessmentEntity userAssessment, String message) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endsAt = userAssessment.getSessionEndsAt();
		boolean active = userAssessment.getSessionStatus() == AssessmentSessionStatus.IN_PROGRESS
				&& endsAt != null
				&& now.isBefore(endsAt);
		long remainingSeconds = active ? Duration.between(now, endsAt).toSeconds() : 0;
		Integer assessmentId = userAssessment.getAssessment() == null ? null : userAssessment.getAssessment().getAssessmentId();
		Integer userId = userAssessment.getUser() == null ? null : userAssessment.getUser().getUserId();
		String status = userAssessment.getSessionStatus() == null ? null : userAssessment.getSessionStatus().name();
		return new AssessmentSessionResponse(userAssessment.getUserAssessmentId(), assessmentId, userId,
				userAssessment.getSessionStartedAt(), endsAt, userAssessment.getSubmittedAt(), status, active,
				remainingSeconds, message);
	}

}
