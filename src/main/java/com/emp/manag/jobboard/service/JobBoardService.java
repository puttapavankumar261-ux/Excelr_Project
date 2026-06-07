package com.emp.manag.jobboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.jobboard.entity.JobBoardEntity;
import com.emp.manag.jobboard.repo.JobBoardRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class JobBoardService {

	@Autowired
	private JobBoardRepo jobBoardRepo;

	public JobBoardEntity save(JobBoardEntity jobBoard) {

		validationJobBoard(jobBoard);

		return jobBoardRepo.save(jobBoard);
	}

	public String updateJobBoard(Integer jobId, JobBoardEntity updatedJobBoard) {
		JobBoardEntity existingJobBoard = jobBoardRepo.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

		existingJobBoard.setJobTitle(updatedJobBoard.getJobTitle());
		existingJobBoard.setJobDescription(updatedJobBoard.getJobDescription());
		existingJobBoard.setJobReferral(updatedJobBoard.getJobReferral());
		existingJobBoard.setLocation(updatedJobBoard.getLocation());
		existingJobBoard.setWorkplace(updatedJobBoard.getWorkplace());
		existingJobBoard.setCompanyname(updatedJobBoard.getCompanyname());
		existingJobBoard.setSalaryRange(updatedJobBoard.getSalaryRange());
		existingJobBoard.setEmploymentType(updatedJobBoard.getEmploymentType());
		existingJobBoard.setPostedDate(updatedJobBoard.getPostedDate());
		existingJobBoard.setApplicationDeadline(updatedJobBoard.getApplicationDeadline());

		jobBoardRepo.save(existingJobBoard);

		return "Updated successfully";
	}

	public String deleteJobBoard(Integer jobId) {
		JobBoardEntity existingJobBoard = jobBoardRepo.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

		jobBoardRepo.delete(existingJobBoard);

		return "Deleted successfully";
	}

	public List<JobBoardEntity> getAllJobBoards() {
		return jobBoardRepo.findAll();
	}

	public JobBoardEntity getJobBoardById(Integer jobId) {
		return jobBoardRepo.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
	}

	public String deleteAllJobBoards() {
		jobBoardRepo.deleteAll();
		return "All job boards deleted successfully";
	}

	public void validationJobBoard(JobBoardEntity jobBoard) {
		if (jobBoard == null) {
			throw new RuntimeException("Job details are required");
		}
		if (jobBoard.getJobTitle() == null || jobBoard.getJobTitle().isEmpty()) {
			throw new RuntimeException("Job title is required");
		}
		if (jobBoard.getCompanyname() == null || jobBoard.getCompanyname().isEmpty()) {
			throw new RuntimeException("Company name is required");
		}
		if (jobBoard.getPostedDate() == null) {
			throw new RuntimeException("Posted date is required");
		}
		if (jobBoard.getApplicationDeadline() == null) {
			throw new RuntimeException("Application deadline is required");
		}
		if (jobBoard.getEmploymentType() == null) {
			throw new RuntimeException("Employment type is required");
		}
		if (jobBoard.getApplicationDeadline().isBefore(jobBoard.getPostedDate())) {
			throw new RuntimeException("Application deadline cannot be before posted date");
		}
	}

}
