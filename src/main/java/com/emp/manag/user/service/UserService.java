package com.emp.manag.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.repo.UserRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public UserEntity saveUser(UserEntity user) {

		validateUser(user);
		attachUserRelations(user);

		user.setStatus(UserEntity.UserStatus.ACTIVE);

		return userRepo.save(user);
	}

	public String updateUser(Integer userId, UserEntity updatedUser) {

		if (userId == null) {
			throw new RuntimeException("User ID is required");
		}

		validateUser(updatedUser);

		UserEntity existingUser = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		existingUser.setFirstName(updatedUser.getFirstName());
		existingUser.setMiddleName(updatedUser.getMiddleName());
		existingUser.setLastName(updatedUser.getLastName());
		existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
		existingUser.setPlaceofBirth(updatedUser.getPlaceofBirth());
		existingUser.setAge(updatedUser.getAge());
		existingUser.setLanguage(updatedUser.getLanguage());
		existingUser.setEmail(updatedUser.getEmail());

		return "User updated successfully";
	}

	public List<UserEntity> getAllUsers() {
		return userRepo.findAll();
	}

	public UserEntity getUserById(Integer userId) {

		if (userId == null) {
			throw new RuntimeException("Employee ID is required");
		}
		return userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
	}

	public String deleteUserById(Integer userId) {

		if (userId == null) {
			throw new RuntimeException("User ID is required");
		}

		UserEntity existingUser = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		userRepo.delete(existingUser);
		return "User with ID " + userId + " deleted successfully";
	}

	public String deleteAllEmployees() {
		userRepo.deleteAll();
		return "All employee records deleted successfully";
	}

	private void validateUser(UserEntity user) {

		if (user == null) {
			throw new RuntimeException("User details are required");
		}

		if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
			throw new RuntimeException("First name is required");
		}
		if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
			throw new RuntimeException("Last name is required");
		}
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new RuntimeException("Email is required");
		}

		if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
			throw new RuntimeException("Phone number is required");
		}
		if (user.getGender() == null || user.getGender().trim().isEmpty()) {
			throw new RuntimeException("Gender is Required");
		}
		if (user.getDateOfBirth() != null && user.getDateOfBirth().isAfter(java.time.LocalDate.now())) {
			throw new RuntimeException("Date of birth cannot be in the future");
		}
		if (user.getAge() != null && user.getAge() < 0) {
			throw new RuntimeException("Age cannot be negative");
		}
	}

	private void attachUserRelations(UserEntity user) {
		if (user.getEmployee() != null) {
			user.getEmployee().setUser(user);
		}
	}
}
