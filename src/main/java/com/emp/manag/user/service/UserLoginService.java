package com.emp.manag.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.entity.UserLoginEntity;
import com.emp.manag.user.repo.UserLoginRepo;
import com.emp.manag.user.repo.UserRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserLoginService {

	@Autowired
	private UserLoginRepo loginRepo;

	@Autowired
	private UserRepo userRepo;

	public UserLoginEntity saveLogin(UserLoginEntity login) {

		validateLogin(login);

		Integer userId = login.getUser().getUserId();

		if (loginRepo.existsByUserUserLoginid(userId)) {
			throw new RuntimeException("Login already exists for employee ID: " + userId);
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

		UserLoginEntity login = getLoginById(loginId);
		loginRepo.delete(login);
		
		return "User login deleted successfully";
	}
	
	public String deleteAllLogins() {
		loginRepo.deleteAll();
		return "All login records deleted successfully";
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
	
}

