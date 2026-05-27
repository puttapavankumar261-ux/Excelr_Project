package com.emp.manag.schedule.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.PublicHolidayEntity;

@Repository
public interface PublicHolidayRepo extends JpaRepository<PublicHolidayEntity, Integer> {
	
	boolean existsByPublicholidayDate(LocalDate publicHolidayDate);

	Optional<PublicHolidayEntity> findByPublicholidayDate(LocalDate publicHolidayDate);

	int countByPublicholidayDateBetween(LocalDate startDate, LocalDate endDate);

}
