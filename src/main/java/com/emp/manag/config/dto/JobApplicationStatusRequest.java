package com.emp.manag.config.dto;

import com.emp.manag.jobboard.entity.JobApplicationEntity.CandidateStatus;

import lombok.Data;

@Data
public class JobApplicationStatusRequest {

	private CandidateStatus status;
}
