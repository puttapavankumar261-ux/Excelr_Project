package com.emp.manag.jobboard.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.jobboard.entity.JobApplicationEntity;
import com.emp.manag.jobboard.entity.JobApplicationEntity.CandidateStatus;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplicationEntity, Integer> {

	  boolean existsByUserUserIdAndJobBoardJobBoardId(
	            Integer userId,
	            Integer jobBoardId);

	    Optional<JobApplicationEntity> findByUserUserIdAndJobBoardJobBoardId(
	            Integer userId,
	            Integer jobBoardId);

	    List<JobApplicationEntity> findByUserUserId(
	            Integer userId);

	    List<JobApplicationEntity> findByJobBoardJobBoardId(
	            Integer jobBoardId);

	    List<JobApplicationEntity> findByStatus(
	            CandidateStatus status);
	}