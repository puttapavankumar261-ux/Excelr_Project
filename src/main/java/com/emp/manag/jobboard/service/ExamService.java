package com.emp.manag.jobboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.jobboard.entity.AssessmentEntity;
import com.emp.manag.jobboard.entity.ExamEntity;
import com.emp.manag.jobboard.repo.AssessmentRepo;
import com.emp.manag.jobboard.repo.ExamRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExamService {

	@Autowired
	private ExamRepo examRepo;

	@Autowired
	private AssessmentRepo assessmentRepo;

	public ExamEntity save(ExamEntity exam) {

		validateExam(exam);

		Integer assessmentId = exam.getAssessment().getAssessmentId();

		AssessmentEntity assessment = assessmentRepo.findById(assessmentId)
				.orElseThrow(() -> new RuntimeException("Assessment not found with id: " + assessmentId));

		exam.setAssessment(assessment);

		return examRepo.save(exam);
	}

	public String updateExam(Integer examId, ExamEntity updatedExam) {

		validateExam(updatedExam);

		ExamEntity existingExam = examRepo.findById(examId)
				.orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

		existingExam.setDurationMinutes(updatedExam.getDurationMinutes());
		existingExam.setExamInstructions(updatedExam.getExamInstructions());
		existingExam.setTotalMarks(updatedExam.getTotalMarks());
		existingExam.setExamName(updatedExam.getExamName());
		existingExam.setExamType(updatedExam.getExamType());
		existingExam.setPassMarks(updatedExam.getPassMarks());
		existingExam.setQuestionPattern(updatedExam.getQuestionPattern());
		existingExam.setStatus(updatedExam.getStatus());
		existingExam.setTotalquestions(updatedExam.getTotalquestions());

		examRepo.save(existingExam);

		return "Exam updated successfully";
	}

	public String deleteExam(Integer examId) {
		ExamEntity existingExam = examRepo.findById(examId)
				.orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

		examRepo.delete(existingExam);

		return "Exam deleted successfully";
	}

	public String deleteAllExams() {
		examRepo.deleteAll();
		return "All exams deleted successfully";
	}

	public ExamEntity getExamById(Integer examId) {
		return examRepo.findById(examId).orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
	}

	public List<ExamEntity> getAllExams() {
		return examRepo.findAll();
	}

	public void validateExam(ExamEntity exam) {
		if (exam == null) {
			throw new RuntimeException("Exam details are required");
		}
		if (exam.getExamName() == null || exam.getExamName().isEmpty()) {
			throw new RuntimeException("Exam name is required");
		}
		if (exam.getExamType() == null || exam.getExamType().isEmpty()) {
			throw new RuntimeException("Exam type is required");
		}
		if (exam.getDurationMinutes() == null) {
			throw new RuntimeException("Duration in minutes is required");
		}
		if (exam.getTotalMarks() == null) {
			throw new RuntimeException("Total marks are required");
		}
		if (exam.getPassMarks() == null) {
			throw new RuntimeException("Pass marks are required");
		}
		if (exam.getQuestionPattern() == null || exam.getQuestionPattern().isEmpty()) {
			throw new RuntimeException("Question pattern is required");
		}
		if (exam.getStatus() == null || exam.getStatus().isEmpty()) {
			throw new RuntimeException("Status is required");
		}

	}

}
