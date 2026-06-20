package com.emp.manag.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.user.entity.UserAssessmentEntity;

@Repository
public interface UserAssessmentRepo extends JpaRepository<UserAssessmentEntity, Integer> {

	boolean existsByJobApplicationJobApplicationIdAndAssessmentAssessmentId(Integer applicationId,
			Integer assessmentId);

}
