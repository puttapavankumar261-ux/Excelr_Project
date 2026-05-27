package com.emp.manag.schedule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.RegularizationEntity;

@Repository
public interface RegularizationRepo extends JpaRepository<RegularizationEntity, Integer> {

	boolean existsByAttendanceAttendanceIdAndRequestedstatus(Integer attendanceId, String string);

}
