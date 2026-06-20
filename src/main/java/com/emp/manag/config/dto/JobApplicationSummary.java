package com.emp.manag.config.dto;

import java.util.Map;

import com.emp.manag.jobboard.entity.JobApplicationEntity.CandidateStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationSummary {

	private long totalApplications;
	private Map<CandidateStatus, Long> applicationsByStatus;
}
