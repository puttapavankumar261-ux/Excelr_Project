package com.emp.manag.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.entity.AttendanceEntity;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceEntity, Integer> {

}
