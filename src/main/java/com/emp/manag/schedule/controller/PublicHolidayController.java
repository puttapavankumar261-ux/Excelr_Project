package com.emp.manag.schedule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.schedule.entity.PublicHolidayEntity;
import com.emp.manag.schedule.service.PublicHolidayService;

@RestController
@RequestMapping("/api/employee-management")
public class PublicHolidayController {

	@Autowired
	private PublicHolidayService publicHolidayService;

	@PostMapping("/savepublicholiday")
	public PublicHolidayEntity savePublicHoliday(@RequestBody PublicHolidayEntity publicHoliday) {
		return publicHolidayService.savePublicHoliday(publicHoliday);
	}

	@PutMapping("/updatepublicholiday/{holidayId}")
	public PublicHolidayEntity updatePublicHoliday(@PathVariable Integer holidayId,
			@RequestBody PublicHolidayEntity publicHoliday) {
		return publicHolidayService.updatePublicHoliday(holidayId, publicHoliday);
	}

	@GetMapping("/getpublicholiday/{holidayId}")
	public PublicHolidayEntity getPublicHolidayById(@PathVariable Integer holidayId) {
		return publicHolidayService.getPublicHolidayById(holidayId);
	}

	@GetMapping("/getallpublicholidays")
	public List<PublicHolidayEntity> getAllPublicHolidays() {
		return publicHolidayService.getAllPublicHolidays();
	}

	@GetMapping("/monthlypublicholidays/{year}/{month}")
	public Integer calculateMonthlyPublicHolidays(@PathVariable Integer year, @PathVariable Integer month) {
		return publicHolidayService.calculateMonthlyPublicHolidays(year, month);
	}

	@DeleteMapping("/deletepublicholiday/{holidayId}")
	public String deletePublicHoliday(@PathVariable Integer holidayId) {
		return publicHolidayService.deletePublicHoliday(holidayId);
	}
}
