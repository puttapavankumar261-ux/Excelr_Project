package com.emp.manag.user.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.config.dto.LoginRequest;
import com.emp.manag.config.dto.SessionResponse;
import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.entity.UserLoginEntity;
import com.emp.manag.user.repo.UserLoginRepo;
import com.emp.manag.user.repo.UserRepo;

import jakarta.servlet.http.HttpSession;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserLoginService {

	private static final int SESSION_TIMEOUT_SECONDS = 30 * 60;

	@Autowired
	private UserLoginRepo loginRepo;

	@Autowired
	private UserRepo userRepo;

	public UserLoginEntity saveLogin(UserLoginEntity login) {

		validateLogin(login);

		Integer userId = login.getUser().getUserId();

		if (loginRepo.existsByUserUserId(userId)) {
			throw new RuntimeException("Login already exists for user ID: " + userId);
		}

		if (loginRepo.existsByUsername(login.getUsername())) {
			throw new RuntimeException("Username already exists: " + login.getUsername());
		}

		UserEntity user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
		
		login.setUser(user);

		if (login.getStatus() == null || login.getStatus().trim().isEmpty()) {
			login.setStatus("ACTIVE");
		}

		return loginRepo.save(login);

	}

	
	public String updateLogin(Integer loginId, UserLoginEntity updatedLogin) {

		if (loginId == null) {
			throw new RuntimeException("Login ID is required");
		}

		validateLogin(updatedLogin);

		UserLoginEntity existingLogin = loginRepo.findById(loginId)
				.orElseThrow(() -> new RuntimeException("Login not found with ID: " + loginId));
		
		loginRepo.findByUsername(updatedLogin.getUsername())
		.filter(login -> !login.getUserLoginId().equals(loginId))
		.ifPresent(login -> {
			throw new RuntimeException("Username already exists: " + updatedLogin.getUsername());
		});

		Integer userId = updatedLogin.getUser().getUserId();
		UserEntity user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
		
		existingLogin.setUser(user);
		existingLogin.setUsername(updatedLogin.getUsername());
		existingLogin.setPasswordhash(updatedLogin.getPasswordhash());
		existingLogin.setEmail(updatedLogin.getEmail());
		existingLogin.setRole(updatedLogin.getRole());
		existingLogin.setStatus(updatedLogin.getStatus());
		existingLogin.setPasswordresttoken(updatedLogin.getPasswordresttoken());
		existingLogin.setPasswordresttokenexpiry(updatedLogin.getPasswordresttokenexpiry());

		loginRepo.save(existingLogin);

		return "User login updated successfully";
	}
	
	public UserLoginEntity getLoginById(Integer loginId) {

		if (loginId == null) {
			throw new RuntimeException("Login ID is required");
		}

		return loginRepo.findById(loginId)
				.orElseThrow(() -> new RuntimeException("Login not found with ID: " + loginId));
	}
	
	public UserLoginEntity getLoginByUsername(String username) {

		if (username == null || username.trim().isEmpty()) {
			throw new RuntimeException("Username is required");
		}

		return loginRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Login not found with username: " + username));
	}
	
	public List<UserLoginEntity> getAllLogins() {
		return loginRepo.findAll();
	}
	
	public String deleteLogin(Integer loginId) {

		if (loginId == null) {
			throw new RuntimeException("Login ID is required");
		}

		UserLoginEntity existingLogin = loginRepo.findById(loginId)
				.orElseThrow(() -> new RuntimeException("Login not found with ID: " + loginId));

		loginRepo.delete(existingLogin);
		
		return "User login deleted successfully";
	}
	
	public String deleteAllLogins() {
		loginRepo.deleteAll();
		return "All login records deleted successfully";
	}

	public SessionResponse login(
	        LoginRequest request,
	        HttpSession session) {

	    if (request == null
	            || request.getUsername() == null
	            || request.getUsername().trim().isEmpty()) {

	        throw new RuntimeException(
	                "Username is required");
	    }

	    if (request.getPassword() == null
	            || request.getPassword().trim().isEmpty()) {

	        throw new RuntimeException(
	                "Password is required");
	    }

	    UserLoginEntity login =
	            loginRepo.findByUsername(
	                    request.getUsername().trim())
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Invalid username or password"));

	    if (!"ACTIVE".equalsIgnoreCase(
	            login.getStatus())) {

	        throw new RuntimeException(
	                "User login is not active");
	    }

	    if (!matchesPassword(
	            request.getPassword(),
	            login.getPasswordhash())) {

	        throw new RuntimeException(
	                "Invalid username or password");
	    }

	    LocalDateTime loginAt =
	            LocalDateTime.now();

	    login.setLastLogin(loginAt);

	    loginRepo.save(login);

	    session.setMaxInactiveInterval(
	            SESSION_TIMEOUT_SECONDS);

	    session.setAttribute(
	            "principalType",
	            "USER");

	    session.setAttribute(
	            "userLoginId",
	            login.getUserLoginId());

	    session.setAttribute(
	            "userId",
	            login.getUser().getUserId());

	    session.setAttribute(
	            "username",
	            login.getUsername());

	    session.setAttribute(
	            "role",
	            login.getRole());

	    session.setAttribute(
	            "loginAt",
	            loginAt);

	    return buildSessionResponse(
	            session,
	            true,
	            "User login successful");
	}

	public SessionResponse getSession(HttpSession session) {
		if (session == null || session.getAttribute("userLoginId") == null) {
			return new SessionResponse(
				    null,       // 1
				    "USER",     // 2
				    null,       // 3
				    null,       // 4
				    null,       // 5
				    
				    null,       // 7
				    null,       // 8
				    false,      // 9
				    "No active user session"  // 10
				);
		}
		return buildSessionResponse(session, true, "User session is active");
	}

	public SessionResponse logout(HttpSession session) {
		SessionResponse response = getSession(session);
		session.invalidate();
		response.setAuthenticated(false);
		response.setMessage("User logged out successfully");
		return response;
	}
	
	public void validateLogin(UserLoginEntity login) {

		if (login == null) {
			throw new RuntimeException("Login details are required");
		}

		if (login.getUser() == null || login.getUser().getUserId() == null) {
			throw new RuntimeException("User ID is required");
		}

		if (login.getUsername() == null || login.getUsername().trim().isEmpty()) {
			throw new RuntimeException("Username is required");
		}

		if (login.getPasswordhash() == null || login.getPasswordhash().trim().isEmpty()) {
			throw new RuntimeException("Password hash is required");
		}

		if (login.getEmail() == null || login.getEmail().trim().isEmpty()) {
			throw new RuntimeException("Email is required");
		}
		
		if (login.getRole() == null || login.getRole().trim().isEmpty()) {
			throw new RuntimeException("Role is required");
		}
		
	}

	private boolean matchesPassword(String rawPassword, String storedPasswordHash) {
		return storedPasswordHash != null && storedPasswordHash.equals(rawPassword);
	}

	private SessionResponse buildSessionResponse(HttpSession session, boolean authenticated, String message) {

	    LocalDateTime loginAt = (LocalDateTime) session.getAttribute("loginAt");
	    LocalDateTime expiresAt = loginAt == null ? null 
	                            : loginAt.plusSeconds(session.getMaxInactiveInterval());

	    return new SessionResponse(
	        session.getId(),                                  // 1 - sessionId
	        "USER",                                           // 2 - principalType
	        (Integer) session.getAttribute("userId"),         // 3 - id
	        (String) session.getAttribute("username"),        // 4 - username
	        (String) session.getAttribute("role"),            // 5 - role
	        
	        loginAt,                                          // 7 - loginAt
	        expiresAt,                                        // 8 - expiresAt
	        authenticated,                                    // 9 - authenticated
	        message                                           // 10 - message
	    );
	}
	
}

