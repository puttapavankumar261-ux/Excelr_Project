package com.emp.manag.jobboard.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.jobboard.entity.AssessmentEntity;

@Repository
public interface AssessmentRepo extends JpaRepository<AssessmentEntity, Integer> {
	
	List<AssessmentEntity> findByJobBoardJobBoardId(Integer jobBoardId);
	

}
