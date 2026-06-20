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

import com.emp.manag.jobboard.entity.ExamEntity;
import com.emp.manag.jobboard.service.ExamService;

@RestController
@RequestMapping("/api/employee-management")
public class ExamController {

	@Autowired
	private ExamService examService;

	@PostMapping("/SaveExam")
	public ExamEntity saveExam(@RequestBody ExamEntity exam) {
		return examService.save(exam);
	}

	@PutMapping("/UpdateExam/{ExamId}")
	public String updateExam(@PathVariable Integer ExamId, @RequestBody ExamEntity updatedExam) {
		return examService.updateExam(ExamId, updatedExam);
	}

	@DeleteMapping("/DeleteExam/{ExamId}")
	public String deleteExam(@PathVariable Integer ExamId) {
		return examService.deleteExam(ExamId);
	}

	@DeleteMapping("/DeleteAllExam")
	public String deleteAllExams() {
		return examService.deleteAllExams();
	}

	@GetMapping("/GetExamById/{ExamId}")
	public ExamEntity getExamById(@PathVariable Integer ExamId) {
		return examService.getExamById(ExamId);
	}

	@GetMapping("/GetAllExam")
	public List<ExamEntity> getAllExams() {
		return examService.getAllExams();
	}
}
