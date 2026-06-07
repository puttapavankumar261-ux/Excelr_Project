package com.emp.manag.jobboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.jobboard.entity.ExamEntity;
import com.emp.manag.jobboard.entity.ExamQuestionEntity;
import com.emp.manag.jobboard.repo.ExamQuestionRepo;
import com.emp.manag.jobboard.repo.ExamRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExamQuestionService {

	@Autowired
	private ExamQuestionRepo questionRepo;

	@Autowired
	private ExamRepo examRepo;

	public ExamQuestionEntity saveQuestion(ExamQuestionEntity question) {

		validateQuestion(question);

		Integer examId = question.getExam().getExamid();

		question.setExam(
				examRepo.findById(examId).orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId)));

		ExamEntity exam = examRepo.findById(examId)
				.orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

		question.setExam(exam);

		return questionRepo.save(question);
	}

	public String updatequestion(Integer questionId, ExamQuestionEntity updatedquestion) {

		validateQuestion(updatedquestion);

		if (questionId == null) {
			throw new RuntimeException("Question ID cannot be null");
		}

		ExamQuestionEntity existingQuestion = questionRepo.findById(questionId)
				.orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

		existingQuestion.setQuestion(updatedquestion.getQuestion());
		existingQuestion.setAnswer(updatedquestion.getAnswer());
		existingQuestion.setCorrectAnswer(updatedquestion.getCorrectAnswer());
		existingQuestion.setQuestionType(updatedquestion.getQuestionType());
		existingQuestion.setMarks(updatedquestion.getMarks());
		existingQuestion.setMarksObtained(updatedquestion.getMarksObtained());
		existingQuestion.setCorrect(updatedquestion.getCorrect());

		questionRepo.save(existingQuestion);

		return "Question updated successfully";
	}

	public String deleteQuestion(Integer questionId) {

		if (questionId == null) {
			throw new RuntimeException("Question ID is required for deletion");
		}

		ExamQuestionEntity existingQuestion = questionRepo.findById(questionId)
				.orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

		questionRepo.delete(existingQuestion);

			return "Question deleted successfully";
		}


	public ExamQuestionEntity getQuestionById(Integer questionId) {
		return questionRepo.findById(questionId)
				.orElseThrow(() -> new RuntimeException("Question not found"));
	}

	public List<ExamQuestionEntity> getAllQuestions() {
		return questionRepo.findAll();
	}

	public String deleteAllQuestions() {
		questionRepo.deleteAll();
		return "All questions deleted successfully";
	}

	public void validateQuestion(ExamQuestionEntity question) {
		if (question == null) {
			throw new RuntimeException("Question details are required");
		}
		if (question.getQuestion() == null || question.getQuestion().isEmpty()) {
			throw new RuntimeException("Question text is required");
		}
		if (question.getAnswer() == null || question.getAnswer().isEmpty()) {
			throw new RuntimeException("Answer is required");
		}
		if (question.getMarks() == null) {
			throw new RuntimeException("Marks are required");
		}
	}

}
