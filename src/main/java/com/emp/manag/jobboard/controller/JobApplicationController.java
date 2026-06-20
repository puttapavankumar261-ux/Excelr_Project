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

import com.emp.manag.config.dto.JobApplicationStatusRequest;
import com.emp.manag.config.dto.JobApplicationSummary;
import com.emp.manag.jobboard.entity.JobApplicationEntity;
import com.emp.manag.jobboard.entity.JobApplicationEntity.CandidateStatus;
import com.emp.manag.jobboard.service.JobApplicationService;

@RestController
@RequestMapping("/api/employee-management")
public class JobApplicationController {

	@Autowired
	private JobApplicationService jobApplicationService;

	@PostMapping("/save-applications")
	public JobApplicationEntity saveApplication(@RequestBody JobApplicationEntity application) {
		return jobApplicationService.save(application);
	}

	@PutMapping("/update-applications/{applicationId}")
	public String updateApplication(@PathVariable Integer applicationId, @RequestBody JobApplicationEntity application) {
		return jobApplicationService.updateApplication(applicationId, application);
	}

	@PutMapping("/update-applications/{applicationId}/status")
	public String updateApplicationStatus(@PathVariable Integer applicationId,
			@RequestBody JobApplicationStatusRequest request) {
		if (request == null) {
			throw new RuntimeException("Application status request is required");
		}
		return jobApplicationService.updateApplicationStatus(applicationId, request.getStatus());
	}

	@GetMapping("/get-applications/{applicationId}")
	public JobApplicationEntity getApplicationById(@PathVariable Integer applicationId) {
		return jobApplicationService.getApplicationById(applicationId);
	}

	@GetMapping("/getAll-job-applications")
	public List<JobApplicationEntity> getAllApplications() {
		return jobApplicationService.getAllApplications();
	}

	@GetMapping("/get-applications/user/{userId}")
	public List<JobApplicationEntity> getApplicationsByUser(@PathVariable Integer userId) {
		return jobApplicationService.getApplicationsByUser(userId);
	}

	@GetMapping("/get-applications/job/{jobId}")
	public List<JobApplicationEntity> getApplicationsByJob(@PathVariable Integer jobId) {
		return jobApplicationService.getApplicationsByJob(jobId);
	}

	@GetMapping("/get-applications/status/{status}")
	public List<JobApplicationEntity> getApplicationsByStatus(@PathVariable CandidateStatus status) {
		return jobApplicationService.getApplicationsByStatus(status);
	}

	@GetMapping("/get-applications/summary")
	public JobApplicationSummary getApplicationSummary() {
		return jobApplicationService.getApplicationSummary();
	}

	@DeleteMapping("/delete-job-applications/{applicationId}")
	public String deleteApplication(@PathVariable Integer applicationId) {
		return jobApplicationService.deleteApplication(applicationId);
	}

	@DeleteMapping("/deleteAll-job-applications")
	public String deleteAllApplications() {
		return jobApplicationService.deleteAllApplications();
	}

}
