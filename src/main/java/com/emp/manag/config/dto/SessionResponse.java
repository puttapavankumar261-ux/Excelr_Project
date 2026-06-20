package com.emp.manag.config.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {
	
	private String sessionId;	
    private String principalType;    // EMPLOYEE or USER
    private Integer id;
    private String username;
	private String role;             // ROLE_HR, ROLE_MANAGER etc
    private String department;       // HR, Software, Sales etc  ← ADD THIS
    private LocalDateTime loginAt;
    private LocalDateTime expiresAt;
    private boolean authenticated;
	private String message;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public SessionResponse(String sessionId, String principalType, Integer id, String username, String role,
			String department, LocalDateTime loginAt, LocalDateTime expiresAt, boolean authenticated, String message) {
		this.sessionId = sessionId;
		this.principalType = principalType;
		this.id = id;
		this.username = username;
		this.role = role;
		this.department = department;
		this.loginAt = loginAt;
		this.expiresAt = expiresAt;
		this.authenticated = authenticated;
		this.message = message;
	}

	public SessionResponse(String sessionId, String principalType, Integer id, String username, String role,
			LocalDateTime loginAt, LocalDateTime expiresAt, boolean authenticated, String message) {
		this(sessionId, principalType, id, username, role, null, loginAt, expiresAt, authenticated, message);
	}

}
