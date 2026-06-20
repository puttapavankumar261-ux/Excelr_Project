package com.emp.manag.jobboard.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.jobboard.entity.ExamEntity;

@Repository
public interface ExamRepo extends JpaRepository<ExamEntity, Integer>{

	List<ExamEntity> findByAssessmentAssessmentId(Integer assessmentId);

}
