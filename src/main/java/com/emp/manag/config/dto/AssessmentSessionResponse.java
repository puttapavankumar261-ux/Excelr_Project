package com.emp.manag.config.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssessmentSessionResponse {

	private Integer userAssessmentId;
	private Integer assessmentId;
	private Integer userId;
	private LocalDateTime startedAt;
	private LocalDateTime endsAt;
	private LocalDateTime submittedAt;
	private String sessionStatus;
	private boolean active;
	private long remainingSeconds;
	private String message;
}
