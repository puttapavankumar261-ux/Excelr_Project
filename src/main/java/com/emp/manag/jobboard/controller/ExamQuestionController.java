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

import com.emp.manag.jobboard.entity.ExamQuestionEntity;
import com.emp.manag.jobboard.service.ExamQuestionService;

@RestController
@RequestMapping("/api/employee-management")
public class ExamQuestionController {

	@Autowired
	private ExamQuestionService examQuestionService;

	@PostMapping("/save-questions")
	public ExamQuestionEntity saveQuestion(@RequestBody ExamQuestionEntity examQuestion) {
		return examQuestionService.saveQuestion(examQuestion);
	}

	@PutMapping("/update-questions/{QuestionId}")
	public String updateQuestion(@PathVariable Integer QuestionId, @RequestBody ExamQuestionEntity examQuestion) {
		System.out.println("Question Data Updated");
		return examQuestionService.updatequestion(QuestionId, examQuestion);
	}

	@GetMapping("/retrieve-question/{QuestionId}")
	public ExamQuestionEntity getQuestionById(@PathVariable Integer QuestionId) {
		return examQuestionService.getQuestionById(QuestionId);
	}

	@DeleteMapping("/delete-questions/{QuestionId}")
	public String deleteQuestion(@PathVariable Integer QuestionId) {
		return examQuestionService.deleteQuestion(QuestionId);
	}

	@GetMapping("/retrieve-all-questions")
	public List<ExamQuestionEntity> getAllQuestions() {
		return examQuestionService.getAllQuestions();
	}

	@DeleteMapping("/delete-all-questions")
	public String deleteAllQuestions() {
		return examQuestionService.deleteAllQuestions();
	}
}
