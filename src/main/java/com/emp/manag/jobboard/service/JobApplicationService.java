package com.emp.manag.jobboard.service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.config.dto.JobApplicationSummary;
import com.emp.manag.jobboard.entity.AssessmentEntity;
import com.emp.manag.jobboard.entity.JobApplicationEntity;
import com.emp.manag.jobboard.entity.JobApplicationEntity.CandidateStatus;
import com.emp.manag.jobboard.entity.JobBoardEntity;
import com.emp.manag.jobboard.repo.AssessmentRepo;
import com.emp.manag.jobboard.repo.JobApplicationRepo;
import com.emp.manag.jobboard.repo.JobBoardRepo;
import com.emp.manag.user.entity.UserAssessmentEntity;
import com.emp.manag.user.entity.UserAssessmentEntity.AssessmentSessionStatus;
import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.repo.UserAssessmentRepo;
import com.emp.manag.user.repo.UserRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class JobApplicationService {

	@Autowired
	private JobApplicationRepo applicationRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JobBoardRepo jobBoardRepo;

	@Autowired
	private AssessmentRepo assessmentRepo;

	@Autowired
	private UserAssessmentRepo userAssessmentRepo;

	public JobApplicationEntity save(JobApplicationEntity application) {

		validateCreateApplication(application);

		Integer userId = application.getUser().getUserId();
		Integer jobBoardId = application.getJobBoard().getJobBoardId();

		if (applicationRepo.existsByUserUserIdAndJobBoardJobBoardId(userId, jobBoardId)) {
			throw new RuntimeException("User has already applied for this job");
		}

		UserEntity user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
		JobBoardEntity job = jobBoardRepo.findById(jobBoardId)
				.orElseThrow(() -> new RuntimeException("Job not found with id: " + jobBoardId));

		if (job.getApplicationDeadline() != null && job.getApplicationDeadline().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Application deadline has passed for this job");
		}

		application.setUser(user);
		application.setJobBoard(job);
		application.setAppliedDate(LocalDateTime.now());
		application.setStatus(CandidateStatus.APPLIED);

		JobApplicationEntity savedApplication = applicationRepo.save(application);

		List<AssessmentEntity> assessments = assessmentRepo.findByJobBoardJobBoardId(jobBoardId);

		if (!assessments.isEmpty()) {
		    AssessmentEntity assessment = assessments.get(0);
		    UserAssessmentEntity userAssessment = new UserAssessmentEntity();

		    userAssessment.setUser(user);   			
		    userAssessment.setAssessment(assessment);
		    userAssessment.setSessionStatus(AssessmentSessionStatus.ASSIGNED);
		    userAssessmentRepo.save(userAssessment);
		    savedApplication.setStatus(CandidateStatus.ASSESSMENT_PENDING);
		    applicationRepo.save(savedApplication);
		}
		
		return savedApplication;
	}

	public String updateApplication(Integer applicationId, JobApplicationEntity updatedApplication) {
		if (updatedApplication == null || updatedApplication.getStatus() == null) {
			throw new RuntimeException("Application status is required");
		}

		return updateApplicationStatus(applicationId, updatedApplication.getStatus());
	}

	public String updateApplicationStatus(Integer applicationId, CandidateStatus nextStatus) {
		JobApplicationEntity existingApplication = getApplicationById(applicationId);

		validateStatusTransition(existingApplication.getStatus(), nextStatus);
		existingApplication.setStatus(nextStatus);
		applicationRepo.save(existingApplication);

		return "Job application moved to " + nextStatus + " successfully";
	}

	public JobApplicationEntity getApplicationById(Integer applicationId) {
		if (applicationId == null) {
			throw new RuntimeException("Job application ID is required");
		}
		return applicationRepo.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Job application not found with id: " + applicationId));
	}

	public List<JobApplicationEntity> getAllApplications() {
		return applicationRepo.findAll();
	}

	public List<JobApplicationEntity> getApplicationsByUser(Integer userId) {
		if (userId == null) {
			throw new RuntimeException("User ID is required");
		}
		if (!userRepo.existsById(userId)) {
			throw new RuntimeException("User not found with id: " + userId);
		}
		return applicationRepo.findByUserUserId(userId);
	}

	public List<JobApplicationEntity> getApplicationsByJob(Integer jobBoardId) {
		if (jobBoardId == null) {
			throw new RuntimeException("Job ID is required");
		}
		if (!jobBoardRepo.existsById(jobBoardId)) {
			throw new RuntimeException("Job not found with id: " + jobBoardId);
		}
		return applicationRepo.findByJobBoardJobBoardId(jobBoardId);
	}

	public List<JobApplicationEntity> getApplicationsByStatus(CandidateStatus status) {
		if (status == null) {
			throw new RuntimeException("Application status is required");
		}
		return applicationRepo.findByStatus(status);
	}

	public String deleteApplication(Integer applicationId) {
		JobApplicationEntity application = getApplicationById(applicationId);
		applicationRepo.delete(application);
		return "Job application deleted successfully";
	}

	public String deleteAllApplications() {
		applicationRepo.deleteAll();
		return "All job applications deleted successfully";
	}

	public JobApplicationSummary getApplicationSummary() {
		List<JobApplicationEntity> applications = applicationRepo.findAll();
		Map<CandidateStatus, Long> applicationsByStatus = new EnumMap<>(CandidateStatus.class);
		for (CandidateStatus status : CandidateStatus.values()) {
			applicationsByStatus.put(status, 0L);
		}
		for (JobApplicationEntity application : applications) {
			CandidateStatus status = application.getStatus();
			if (status != null) {
				applicationsByStatus.put(status, applicationsByStatus.get(status) + 1);
			}
		}
		return new JobApplicationSummary(applications.size(), applicationsByStatus);
	}

	private void validateCreateApplication(JobApplicationEntity application) {
		if (application == null) {
			throw new RuntimeException("Job application details are required");
		}
		if (application.getUser() == null || application.getUser().getUserId() == null) {
			throw new RuntimeException("Valid user is required for job application");
		}
		if (application.getJobBoard() == null || application.getJobBoard().getJobBoardId() == null) {
			throw new RuntimeException("Valid job is required for job application");
		}
	}

	private void validateStatusTransition(CandidateStatus currentStatus, CandidateStatus nextStatus) {
		if (nextStatus == null) {
			throw new RuntimeException("Next application status is required");
		}
		if (currentStatus == null) {
			throw new RuntimeException("Current application status is missing");
		}
		if (currentStatus == nextStatus) {
			throw new RuntimeException("Application is already in " + nextStatus + " status");
		}
		if (isFinalStatus(currentStatus)) {
			throw new RuntimeException("Application is already closed with status " + currentStatus);
		}
		if (!allowedNextStatuses(currentStatus).contains(nextStatus)) {
			throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + nextStatus);
		}
	}

	private boolean isFinalStatus(CandidateStatus status) {
		return status == CandidateStatus.ASSESSMENT_FAILED || status == CandidateStatus.INTERVIEW_REJECTED
				|| status == CandidateStatus.ONBOARDED;
	}

	private EnumSet<CandidateStatus> allowedNextStatuses(CandidateStatus status) {
		return switch (status) {
		case REGISTERED -> EnumSet.of(CandidateStatus.APPLIED);
		case APPLIED -> EnumSet.of(CandidateStatus.ASSESSMENT_PENDING, CandidateStatus.INTERVIEW_SCHEDULED);
		case ASSESSMENT_PENDING -> EnumSet.of(CandidateStatus.ASSESSMENT_COMPLETED, CandidateStatus.ASSESSMENT_FAILED);
		case ASSESSMENT_COMPLETED -> EnumSet.of(CandidateStatus.ASSESSMENT_PASSED, CandidateStatus.ASSESSMENT_FAILED);
		case ASSESSMENT_PASSED -> EnumSet.of(CandidateStatus.INTERVIEW_SCHEDULED);
		case INTERVIEW_SCHEDULED -> EnumSet.of(CandidateStatus.INTERVIEW_SELECTED, CandidateStatus.INTERVIEW_REJECTED);
		case INTERVIEW_SELECTED -> EnumSet.of(CandidateStatus.OFFER_RELEASED);
		case OFFER_RELEASED -> EnumSet.of(CandidateStatus.OFFER_ACCEPTED);
		case OFFER_ACCEPTED -> EnumSet.of(CandidateStatus.ONBOARDED);
		case ASSESSMENT_FAILED, INTERVIEW_REJECTED, ONBOARDED -> EnumSet.noneOf(CandidateStatus.class);
		};
	}

}
