package com.emp.manag.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.entity.UserExperienceEntity;
import com.emp.manag.user.repo.UserExperienceRepo;
import com.emp.manag.user.repo.UserRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserExperienceService {

	@Autowired
	private UserExperienceRepo userExpRepo;

	@Autowired
	private UserRepo userRepo;

	public UserExperienceEntity saveExperience(UserExperienceEntity experience) {

		validateExperience(experience);

		Integer userId = experience.getUser().getUserId();
		UserEntity user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		experience.setUser(user);

		return userExpRepo.save(experience);
	}

	public String updateExperience(Integer experienceId, UserExperienceEntity updatedexperience) {
		validateExperience(updatedexperience);

		if (experienceId == null) {
			throw new RuntimeException("Experience ID is required");
		}

		validateExperience(updatedexperience);

		UserExperienceEntity existingExperience = userExpRepo.findById(experienceId)
				.orElseThrow(() -> new RuntimeException("Experience not found with ID: " + experienceId));

		existingExperience.setCompanyName(updatedexperience.getCompanyName());
		existingExperience.setJobTitle(updatedexperience.getJobTitle());
		existingExperience.setStartDate(updatedexperience.getStartDate());
		existingExperience.setEndDate(updatedexperience.getEndDate());
		existingExperience.setCurrentlyWorking(updatedexperience.getCurrentlyWorking());
		existingExperience.setRolesAndResponsibilities(updatedexperience.getRolesAndResponsibilities());
		existingExperience.setLocation(updatedexperience.getLocation());
		existingExperience.setSalary(updatedexperience.getSalary());
		existingExperience.setWorkplace(updatedexperience.getWorkplace());
		existingExperience.setReasonForLeaving(updatedexperience.getReasonForLeaving());
		existingExperience.setSkills(updatedexperience.getSkills());

		userExpRepo.save(existingExperience);
		return "Experience updated successfully";
	}

	public String deleteExperience(Integer experienceId) {
		UserExperienceEntity existingExperience = userExpRepo.findById(experienceId)
				.orElseThrow(() -> new RuntimeException("Experience not found with ID: " + experienceId));
		userExpRepo.delete(existingExperience);
		return "Experience deleted successfully";
	}

	public String deleteAllExperiences() {
		userExpRepo.deleteAll();
		return "All experiences deleted successfully";
	}

	public UserExperienceEntity getExperienceById(Integer experienceId) {
		return userExpRepo.findById(experienceId)
				.orElseThrow(() -> new RuntimeException("Experience not found with ID: " + experienceId));
	}

	public List<UserExperienceEntity> getAllExperiences() {
		return userExpRepo.findAll();
	}

	private void validateExperience(UserExperienceEntity experience) {
		if (experience.getCompanyName() == null || experience.getCompanyName().isEmpty()) {
			throw new IllegalArgumentException("Company name is required");
		}
		if (experience.getJobTitle() == null || experience.getJobTitle().isEmpty()) {
			throw new IllegalArgumentException("Job title is required");
		}
		if (experience.getStartDate() == null || experience.getStartDate().isEmpty()) {
			throw new IllegalArgumentException("Start date is required");
		}
		if (experience.getEndDate() == null || experience.getEndDate().isEmpty()) {
			throw new IllegalArgumentException("End date is required");
		}
		if (experience.getCurrentlyWorking() == null || experience.getCurrentlyWorking().isEmpty()) {
			throw new IllegalArgumentException("Currently working status is required");
		}
		if (experience.getRolesAndResponsibilities() == null || experience.getRolesAndResponsibilities().isEmpty()) {
			throw new IllegalArgumentException("Roles and responsibilities are required");
		}
		if (experience.getLocation() == null || experience.getLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}
		if (experience.getSalary() == null) {
			throw new IllegalArgumentException("Salary is required");
		}
		if (experience.getWorkplace() == null || experience.getWorkplace().isEmpty()) {
			throw new IllegalArgumentException("Workplace type is required");
		}
		if (experience.getReasonForLeaving() == null || experience.getReasonForLeaving().isEmpty()) {
			throw new IllegalArgumentException("Reason for leaving is required");
		}
		if (experience.getSkills() == null || experience.getSkills().isEmpty()) {
			throw new IllegalArgumentException("Skills are required");
		}

	}
}
