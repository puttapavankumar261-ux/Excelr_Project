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

import com.emp.manag.jobboard.entity.JobBoardEntity;
import com.emp.manag.jobboard.service.JobBoardService;

@RestController
@RequestMapping("/api/employee-management")
public class JobBoardController {

	@Autowired
	private JobBoardService jobBoardService;

	@PostMapping("/job-board")
	public JobBoardEntity saveJob(@RequestBody JobBoardEntity jobBoard) {
		System.out.println("JobBoardController.saveJob() called with: " + jobBoard);
		return jobBoardService.save(jobBoard);
	}

	@PutMapping("/job-board/{jobBoardId}")
	public String updateJobBoard(@PathVariable Integer jobId, @RequestBody JobBoardEntity updatedJobBoard) {
		jobBoardService.updateJobBoard(jobId, updatedJobBoard);
		return "Job board updated successfully";
	}

	@GetMapping("/getAll-job-board")
	public List<JobBoardEntity> getAllJobBoards() {
		return jobBoardService.getAllJobBoards();
	}

	@GetMapping("/job-board/{jobBoardId}")
	public JobBoardEntity getJobBoardById(@PathVariable Integer jobId) {
		return jobBoardService.getJobBoardById(jobId);
	}

	@DeleteMapping("/job-board/{jobBoardId}")
	public String deleteJobBoard(@PathVariable Integer jobId) {
		return jobBoardService.deleteJobBoard(jobId);
	}

	@DeleteMapping("/job-board")
	public String deleteAllJobBoards() {
		return jobBoardService.deleteAllJobBoards();
	}
}
