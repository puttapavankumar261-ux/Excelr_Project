package com.emp.manag.employee.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.EmpLoginEntity;
import com.emp.manag.employee.repo.EmpLoginRepo;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.config.dto.LoginRequest;
import com.emp.manag.config.dto.SessionResponse;

import jakarta.servlet.http.HttpSession;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmpLoginService {

	private static final int SESSION_TIMEOUT_SECONDS = 30 * 60;

	@Autowired
	private EmpLoginRepo loginRepo;

	@Autowired
	private EmpRepo empRepo;

	public EmpLoginEntity saveLogin(EmpLoginEntity login) {

		validateLogin(login);

		Integer employeeId = login.getEmployee().getEmployeeid();

		if (loginRepo.existsByEmployeeEmployeeid(employeeId)) {
			throw new RuntimeException("Login already exists for employee ID: " + employeeId);
		}

		if (loginRepo.existsByUsername(login.getUsername())) {
			throw new RuntimeException("Username already exists: " + login.getUsername());
		}

		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		login.setEmployee(employee);

		if (login.getStatus() == null || login.getStatus().trim().isEmpty()) {
			login.setStatus("ACTIVE");
		}

		return loginRepo.save(login);
	}

	public String updateLogin(Integer loginId, EmpLoginEntity updatedLogin) {

		if (loginId == null) {
			throw new RuntimeException("Login ID is required");
		}

		validateLogin(updatedLogin);

		EmpLoginEntity existingLogin = loginRepo.findById(loginId)
				.orElseThrow(() -> new RuntimeException("Login not found with ID: " + loginId));

		loginRepo.findByUsername(updatedLogin.getUsername())
				.filter(login -> !login.getLoginid().equals(loginId))
				.ifPresent(login -> {
					throw new RuntimeException("Username already exists: " + updatedLogin.getUsername());
				});

		Integer employeeId = updatedLogin.getEmployee().getEmployeeid();
		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		existingLogin.setEmployee(employee);
		existingLogin.setUsername(updatedLogin.getUsername());
		existingLogin.setPasswordHash(updatedLogin.getPasswordHash());
		existingLogin.setRole(updatedLogin.getRole());
		existingLogin.setStatus(updatedLogin.getStatus());
		existingLogin.setPasswordResetToken(updatedLogin.getPasswordResetToken());
		existingLogin.setPasswordResetExpiry(updatedLogin.getPasswordResetExpiry());

		loginRepo.save(existingLogin);
		return "Login record updated successfully";
	}

	public EmpLoginEntity getLoginById(Integer loginId) {

		if (loginId == null) {
			throw new RuntimeException("Login ID is required");
		}

		return loginRepo.findById(loginId)
				.orElseThrow(() -> new RuntimeException("Login not found with ID: " + loginId));
	}

	public EmpLoginEntity getLoginByUsername(String username) {

		if (username == null || username.trim().isEmpty()) {
			throw new RuntimeException("Username is required");
		}

		return loginRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Login not found for username: " + username));
	}

	public List<EmpLoginEntity> getAllLogins() {
		return loginRepo.findAll();
	}

	public String deleteLogin(Integer loginId) {

		EmpLoginEntity login = getLoginById(loginId);
		loginRepo.delete(login);

		return "Login record deleted successfully";
	}

	public String deleteAllLogins() {
		loginRepo.deleteAll();
		return "All login records deleted successfully";
	}

	public SessionResponse login(LoginRequest request, HttpSession session) {
		if (request == null || request.getUsername() == null || request.getUsername().trim().isEmpty()) {
			throw new RuntimeException("Username is required");
		}
		if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
			throw new RuntimeException("Password is required");
		}

		EmpLoginEntity login = loginRepo.findByUsername(request.getUsername().trim())
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));

		if (!"ACTIVE".equalsIgnoreCase(login.getStatus())) {
			throw new RuntimeException("Employee login is not active");
		}
		if (!matchesPassword(request.getPassword(), login.getPasswordHash())) {
			throw new RuntimeException("Invalid username or password");
		}

		LocalDateTime loginAt = LocalDateTime.now();
		login.setLastLogin(loginAt);
		loginRepo.save(login);

		session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
		session.setAttribute("principalType", "EMPLOYEE");
		session.setAttribute("employeeLoginId", login.getLoginid());
		session.setAttribute("employeeId", login.getEmployee().getEmployeeid());
		session.setAttribute("username", login.getUsername());
		session.setAttribute("role", login.getRole());
		session.setAttribute("loginAt", loginAt);
		session.setAttribute("department", login.getEmployee().getDepartment() == null
				? null : login.getEmployee().getDepartment().name());

		return buildSessionResponse(session, true, "Employee login successful");
	}

	public SessionResponse getSession(HttpSession session) {
	    if (session == null || session.getAttribute("employeeLoginId") == null) {
	        return new SessionResponse(
	            null,           // sessionId
	            "EMPLOYEE",     // principalType
	            null,           // id
	            null,           // username
	            null,           // role
	            null,           // department  ← add this null
	            null,           // loginAt
	            null,           // expiresAt
	            false,          // authenticated
	            "No active employee session"  // message
	        );
	    }
	    return buildSessionResponse(session, true, "Employee session is active");
	}

	public SessionResponse logout(HttpSession session) {
		SessionResponse response = getSession(session);
		session.invalidate();
		response.setAuthenticated(false);
		response.setMessage("Employee logged out successfully");
		return response;
	}

	private void validateLogin(EmpLoginEntity login) {

		if (login == null) {
			throw new RuntimeException("Login details are required");
		}

		if (login.getEmployee() == null || login.getEmployee().getEmployeeid() == null) {
			throw new RuntimeException("Employee ID is required");
		}

		if (login.getUsername() == null || login.getUsername().trim().isEmpty()) {
			throw new RuntimeException("Username is required");
		}

		if (login.getPasswordHash() == null || login.getPasswordHash().trim().isEmpty()) {
			throw new RuntimeException("Password hash is required");
		}

		if (login.getRole() == null || login.getRole().trim().isEmpty()) {
			throw new RuntimeException("Login role is required");
		}
	}

	private boolean matchesPassword(String rawPassword, String storedPasswordHash) {
		return storedPasswordHash != null && storedPasswordHash.equals(rawPassword);
	}

	private SessionResponse buildSessionResponse(HttpSession session,
			boolean authenticated, String message) {
		
		LocalDateTime loginAt = (LocalDateTime) session.getAttribute("loginAt");
		LocalDateTime expiresAt = loginAt == null ? null : 
			loginAt.plusSeconds(session.getMaxInactiveInterval());
		
		  return new SessionResponse(
			        session.getId(),                                        // 1 - sessionId
			        "EMPLOYEE",                                            // 2 - principalType
			        (Integer) session.getAttribute("employeeId"),          // 3 - id
			        (String) session.getAttribute("username"),             // 4 - username
			        (String) session.getAttribute("role"),                 // 5 - role
			        (String) session.getAttribute("department"),           // 6 - department
			        loginAt,                                               // 7 - loginAt
			        expiresAt,                                             // 8 - expiresAt
			        authenticated,                                         // 9 - authenticated
			        message                                                // 10 - message
			    );
}

}
