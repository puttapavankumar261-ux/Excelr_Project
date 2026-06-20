package com.emp.manag.jobboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.jobboard.entity.JobBoardEntity;

@Repository
public interface JobBoardRepo extends JpaRepository<JobBoardEntity, Integer> {

}
