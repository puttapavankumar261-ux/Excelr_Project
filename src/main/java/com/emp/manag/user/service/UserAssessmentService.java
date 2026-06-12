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
	private AssessmentRepo assessmentRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JobApplicationRepo jobApplicationRepo;

	@Autowired
	private ExamRepo examRepo;
	

	public UserAssessmentEntity saveUserAssessment(
	        UserAssessmentEntity userAssessment) {

	    validateUserAssessment(userAssessment);

	    Integer userId =
	            userAssessment.getUser().getUserId();

	    Integer applicationId =userAssessment.getJobApplication().getJobApplicationId();

	    Integer assessmentId = userAssessment.getAssessment().getAssessmentId();

	    UserEntity user = userRepo.findById(userId)
	            .orElseThrow(() ->  new RuntimeException("User not found with ID: " + userId));

	    JobApplicationEntity application =
	    		jobApplicationRepo.findById(applicationId)
	                    .orElseThrow(() -> new RuntimeException("Job Application not found with ID: "+ applicationId));

	    AssessmentEntity assessment =
	    		assessmentRepo.findById(assessmentId)
	                    .orElseThrow(() -> new RuntimeException("Assessment not found with ID: "+ assessmentId));

	    /*
	     * Verify application belongs to user
	     */
	    if (!application.getUser().getUserId()
	            .equals(user.getUserId())) {

	        throw new RuntimeException("Selected application does not belong to the user");
	    }

	    /*
	     * Verify assessment belongs to same job
	     */
	    if (!assessment.getJobBoard().getJobBoardId()
	            .equals(application.getJobBoard().getJobBoardId())) {

	        throw new RuntimeException("Assessment does not belong to the application's job");
	    }

	    /*
	     * Prevent duplicate assignment
	     */
	    if (userAssessmentRepo.existsByJobApplicationJobApplicationIdAndAssessmentAssessmentId(applicationId,assessmentId)) {

	        throw new RuntimeException("Assessment already assigned to this application");
	    }

	    userAssessment.setUser(user);
	    userAssessment.setJobApplication(application);
	    userAssessment.setAssessment(assessment);

	    if (userAssessment.getSessionStatus() == null) {
	        userAssessment.setSessionStatus(
	                AssessmentSessionStatus.ASSIGNED);
	    }

	    return userAssessmentRepo.save(userAssessment);
	}
	
	public String updateAssesment(Integer userAssessmentId, UserAssessmentEntity updatedUserAssessment) {

		if (userAssessmentId == null) {
			throw new RuntimeException("User Assessment ID is required");
		}	
		
		if(updatedUserAssessment.getScore() != null) {

		    if(updatedUserAssessment.getScore() < 0) {
		        throw new RuntimeException("Score cannot be negative");
		    }

		    if(updatedUserAssessment.getScore() > 100) {
		        throw new RuntimeException("Score cannot exceed 100");
		    }
		}
		
		  validateUpdateAssessment(updatedUserAssessment);

		UserAssessmentEntity existingUserAssessment = userAssessmentRepo.findById(userAssessmentId)
				.orElseThrow(() -> new RuntimeException("User Assessment not found with ID: " + userAssessmentId));

		existingUserAssessment.setScore(updatedUserAssessment.getScore());
		existingUserAssessment.setPassed(updatedUserAssessment.getPassed());		
		
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
		
		if(userAssessment.getSessionStatus()!= AssessmentSessionStatus.ASSIGNED) {

		    throw new RuntimeException(
		        "Assessment is not available for starting");
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

	private void validateUserAssessment(
	        UserAssessmentEntity userAssessment) {

	    if (userAssessment == null) {
	    	throw new RuntimeException("User assessment details are required");
	    }

	    if (userAssessment.getUser() == null|| userAssessment.getUser().getUserId() == null) {

	        throw new RuntimeException("Valid user is required");
	    }

	    if (userAssessment.getJobApplication() == null|| userAssessment.getJobApplication()
	                    .getJobApplicationId() == null) {

	        throw new RuntimeException("Job Application is required");
	    }

	    if (userAssessment.getAssessment() == null
	            || userAssessment.getAssessment().getAssessmentId() == null) {

	        throw new RuntimeException("Assessment is required");
	    }
	}
	
	private void validateUpdateAssessment(
	        UserAssessmentEntity assessment) {

	    if (assessment == null) {
	        throw new RuntimeException("Assessment data is required");
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
