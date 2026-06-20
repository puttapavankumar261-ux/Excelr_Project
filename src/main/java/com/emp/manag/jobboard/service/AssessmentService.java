package com.emp.manag.jobboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.jobboard.entity.AssessmentEntity;
import com.emp.manag.jobboard.entity.JobBoardEntity;
import com.emp.manag.jobboard.repo.AssessmentRepo;
import com.emp.manag.jobboard.repo.JobBoardRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class AssessmentService {

	@Autowired
	private AssessmentRepo assessmentRepository;

	@Autowired
	private JobBoardRepo jobBoardRepo;

	public AssessmentEntity save(AssessmentEntity assessment) {

		validateAssessment(assessment);

		Integer jobBoardId = assessment.getJobBoard().getJobBoardId();

		JobBoardEntity job = jobBoardRepo.findById(jobBoardId)
				.orElseThrow(() -> new RuntimeException("Job not found with id: " + jobBoardId));
		assessment.setJobBoard(job);
		return assessmentRepository.save(assessment);
	}

	public String updateAssessment(Integer assessmentId, AssessmentEntity updatedassessment) {

		validateAssessment(updatedassessment);

		if (assessmentId == null) {
			throw new RuntimeException("Assessment ID is required for update");
		}

		if (updatedassessment == null) {
			throw new RuntimeException("Assessment details are required for update");
		}

		AssessmentEntity existingAssessment = assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new RuntimeException("Assessment not found with id: " + assessmentId));

		existingAssessment.setFeedback(updatedassessment.getFeedback());
		existingAssessment.setJobBoard(updatedassessment.getJobBoard());
		existingAssessment.setAssessmentResult(updatedassessment.getAssessmentResult());

		assessmentRepository.save(existingAssessment);

		return "Updated Successfully";
	}

	public AssessmentEntity getAssessmentById(Integer assessmentId) {

		return assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new RuntimeException("Assessment not found with id: " + assessmentId));
	}

	public String deleteAssessment(Integer assessmentId) {

		if (assessmentId == null) {
			throw new RuntimeException("Assessment ID is required for deletion");
		}

		AssessmentEntity existingAssessment = assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new RuntimeException("Assessment not found with id: " + assessmentId));

		assessmentRepository.delete(existingAssessment);

		return "Record deleted successfully";
	}

	public List<AssessmentEntity> getAllAssessments() {

		return assessmentRepository.findAll();
	}

	public void validateAssessment(AssessmentEntity assessment) {
		if (assessment == null) {
			throw new RuntimeException("Assessment details are required");
		}
		if (assessment.getTotalScore() == null) {
			throw new RuntimeException("Total Score is required");
		}
		if (assessment.getFeedback() == null || assessment.getFeedback().isEmpty()) {
			throw new RuntimeException("Feedback is required");
		}
		if (assessment.getJobBoard() == null || assessment.getJobBoard().getJobBoardId() == null) {
			throw new RuntimeException("Valid Job is required");
		}

	}

	public String deleteAllAssessments() {
		assessmentRepository.deleteAll();
		return "All assessments deleted successfully";
	}

}
