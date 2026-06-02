package com.emp.manag.config.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionResponse {

	private String sessionId;
	private String principalType;
	private Integer principalId;
	private String username;
	private String role;
	private LocalDateTime loginAt;
	private LocalDateTime expiresAt;
	private boolean authenticated;
	private String message;
}
