package com.emp.manag.schedule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.ShiftEntity;


@Repository
public interface ShiftRepo extends JpaRepository<ShiftEntity, Integer> {
	
}