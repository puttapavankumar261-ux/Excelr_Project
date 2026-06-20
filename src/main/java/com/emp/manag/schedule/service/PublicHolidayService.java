package com.emp.manag.schedule.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.schedule.entity.PublicHolidayEntity;
import com.emp.manag.schedule.repo.PublicHolidayRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class PublicHolidayService {

	@Autowired
	private PublicHolidayRepo publicHolidayRepo;

	public PublicHolidayEntity savePublicHoliday(PublicHolidayEntity publicHoliday) {

		validatePublicHoliday(publicHoliday);

		if (publicHolidayRepo.existsByPublicholidayDate(publicHoliday.getPublicholidayDate())) {
			throw new RuntimeException("Public holiday already exists on " + publicHoliday.getPublicholidayDate());
		}

		return publicHolidayRepo.save(publicHoliday);
	}

	public PublicHolidayEntity updatePublicHoliday(Integer holidayId, PublicHolidayEntity updatedHoliday) {

		if (holidayId == null) {
			throw new RuntimeException("Holiday ID is required");
		}

		validatePublicHoliday(updatedHoliday);

		PublicHolidayEntity existingHoliday = publicHolidayRepo.findById(holidayId)
				.orElseThrow(() -> new RuntimeException("Public holiday not found with ID: " + holidayId));

		publicHolidayRepo.findByPublicholidayDate(updatedHoliday.getPublicholidayDate())
				.filter(holiday -> !holiday.getHolidayId().equals(holidayId))
				.ifPresent(holiday -> {
					throw new RuntimeException("Public holiday already exists on " + updatedHoliday.getPublicholidayDate());
				});

		existingHoliday.setPublicholidayName(updatedHoliday.getPublicholidayName());
		existingHoliday.setPublicholidayDate(updatedHoliday.getPublicholidayDate());

		return publicHolidayRepo.save(existingHoliday);
	}

	public PublicHolidayEntity getPublicHolidayById(Integer holidayId) {

		if (holidayId == null) {
			throw new RuntimeException("Holiday ID is required");
		}

		return publicHolidayRepo.findById(holidayId)
				.orElseThrow(() -> new RuntimeException("Public holiday not found with ID: " + holidayId));
	}

	public List<PublicHolidayEntity> getAllPublicHolidays() {
		return publicHolidayRepo.findAll();
	}

	public Integer calculateMonthlyPublicHolidays(Integer year, Integer month) {

		YearMonth yearMonth = validateYearMonth(year, month);

		return publicHolidayRepo.countByPublicholidayDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth());
	}

	public String deletePublicHoliday(Integer holidayId) {

		PublicHolidayEntity existingHoliday = getPublicHolidayById(holidayId);
		publicHolidayRepo.delete(existingHoliday);

		return "Public holiday deleted successfully";
	}

	private void validatePublicHoliday(PublicHolidayEntity publicHoliday) {

		if (publicHoliday == null) {
			throw new RuntimeException("Public holiday details are required");
		}

		if (publicHoliday.getPublicholidayName() == null || publicHoliday.getPublicholidayName().trim().isEmpty()) {
			throw new RuntimeException("Public holiday name is required");
		}

		if (publicHoliday.getPublicholidayDate() == null) {
			throw new RuntimeException("Public holiday date is required");
		}

		if (publicHoliday.getPublicholidayDate().isBefore(LocalDate.of(2000, 1, 1))) {
			throw new RuntimeException("Public holiday date is invalid");
		}
	}

	private YearMonth validateYearMonth(Integer year, Integer month) {

		if (year == null || month == null) {
			throw new RuntimeException("Year and month are required");
		}

		if (month < 1 || month > 12) {
			throw new RuntimeException("Month must be between 1 and 12");
		}

		return YearMonth.of(year, month);
	}
}
