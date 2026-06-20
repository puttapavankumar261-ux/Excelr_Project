package com.emp.manag.schedule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.WeekOffPolicyEntity;

@Repository
public interface WeekOffPolicyRepo extends JpaRepository<WeekOffPolicyEntity, Integer> {

	boolean existsByPolicyNameIgnoreCase(String policyName);
}
