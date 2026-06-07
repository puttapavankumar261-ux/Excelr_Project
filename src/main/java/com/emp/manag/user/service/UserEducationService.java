package com.emp.manag.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.user.entity.UserEducationEntity;
import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.repo.UserEducationRepo;
import com.emp.manag.user.repo.UserRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserEducationService {

	@Autowired
	private UserEducationRepo educationRepo;
	
	@Autowired
	private UserRepo userRepo;

	public UserEducationEntity saveEducation(UserEducationEntity education) {

		Integer userId = education.getUser().getUserId();
		UserEntity user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		education.setUser(user);
		
		validateEducation(education);

		return educationRepo.save(education);
	}

	public String updateEducation(Integer educationId, UserEducationEntity updatedEducation) {

		if (educationId == null) {
			throw new RuntimeException("Education ID is required");
		}

		validateEducation(updatedEducation);

		UserEducationEntity existingEducation = educationRepo.findById(educationId)
				.orElseThrow(() -> new RuntimeException("Education not found with ID: " + educationId));

		existingEducation.setEducationType(updatedEducation.getEducationType());
		existingEducation.setEducationLevel(updatedEducation.getEducationLevel());
		existingEducation.setCourse(updatedEducation.getCourse());
		existingEducation.setSpecialization(updatedEducation.getSpecialization());
		existingEducation.setUniversity(updatedEducation.getUniversity());
		existingEducation.setYearOfPassing(updatedEducation.getYearOfPassing());
		existingEducation.setPercentage(updatedEducation.getPercentage());
		existingEducation.setGrade(updatedEducation.getGrade());
		existingEducation.setLearningMode(updatedEducation.getLearningMode());
		existingEducation.setLocation(updatedEducation.getLocation());

		return "User education updated successfully";
	}

	public List<UserEducationEntity> getAllEducations() {
		return educationRepo.findAll();
	}

	public UserEducationEntity getEducationById(Integer educationId) {

		if (educationId == null) {
			throw new RuntimeException("Education ID is required");
		}

		return educationRepo.findById(educationId)
				.orElseThrow(() -> new RuntimeException("Education not found with ID: " + educationId));
	}

	public String deleteEducationById(Integer educationId) {

		if (educationId == null) {
			throw new RuntimeException("Education ID is required");
		}

		educationRepo.deleteById(educationId);
		return "User education deleted successfully";
	}

	public String deleteAllEducations() {
		educationRepo.deleteAll();
		return "All user educations deleted successfully";
	}

	private void validateEducation(UserEducationEntity education) {

		if (education == null) {
			throw new RuntimeException("Education details are required");
		}

		if (education.getEducationType() == null || education.getEducationType().isEmpty()) {
			throw new RuntimeException("Education type is required");
		}

		if (education.getEducationLevel() == null || education.getEducationLevel().isEmpty()) {
			throw new RuntimeException("Education level is required");
		}

		if (education.getCourse() == null || education.getCourse().isEmpty()) {
			throw new RuntimeException("Course is required");
		}

		if (education.getUniversity() == null || education.getUniversity().isEmpty()) {
			throw new RuntimeException("University is required");
		}

		if (education.getYearOfPassing() == null) {
			throw new RuntimeException("Year of passing is required");
		}

		if (education.getPercentage() == null) {
			throw new RuntimeException("Percentage is required");
		}

		if (education.getGrade() == null || education.getGrade().isEmpty()) {
			throw new RuntimeException("Grade is required");
		}

		if (education.getLearningMode() == null || education.getLearningMode().isEmpty()) {
			throw new RuntimeException("Learning mode is required");
		}

		if (education.getLocation() == null || education.getLocation().isEmpty()) {
			throw new RuntimeException("Location is required");
		}

		if (education.getUser() == null) {
			throw new RuntimeException("User ID is required for education details");
		}
	}

}
