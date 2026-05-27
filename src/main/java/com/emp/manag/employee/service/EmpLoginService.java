package com.emp.manag.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.EmpLoginEntity;
import com.emp.manag.employee.repo.EmpLoginRepo;
import com.emp.manag.employee.repo.EmpRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmpLoginService {

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
		existingLogin.setPassword_reset_token(updatedLogin.getPassword_reset_token());
		existingLogin.setPassword_reset_expiry(updatedLogin.getPassword_reset_expiry());

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
}
