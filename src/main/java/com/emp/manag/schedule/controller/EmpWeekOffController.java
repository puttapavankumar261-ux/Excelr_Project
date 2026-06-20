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

import com.emp.manag.schedule.entity.EmpWeekOffEntity;
import com.emp.manag.schedule.service.EmpWeekOffService;

@RestController
@RequestMapping("/api/employee-management")
public class EmpWeekOffController {

	@Autowired
	private EmpWeekOffService empWeekOffService;

	@PostMapping("/saveempweekoff")
	public EmpWeekOffEntity saveEmpWeekOff(@RequestBody EmpWeekOffEntity empWeekOff) {
		return empWeekOffService.saveEmpWeekOff(empWeekOff);
	}

	@PutMapping("/updateempweekoff/{weekOffId}")
	public EmpWeekOffEntity updateEmpWeekOff(@PathVariable Integer weekOffId,
			@RequestBody EmpWeekOffEntity empWeekOff) {
		return empWeekOffService.updateEmpWeekOff(weekOffId, empWeekOff);
	}

	@GetMapping("/getempweekoff/{weekOffId}")
	public EmpWeekOffEntity getEmpWeekOffById(@PathVariable Integer weekOffId) {
		return empWeekOffService.getEmpWeekOffById(weekOffId);
	}

	@GetMapping("/getallempweekoffs")
	public List<EmpWeekOffEntity> getAllEmpWeekOffs() {
		return empWeekOffService.getAllEmpWeekOffs();
	}

	@GetMapping("/getemployeeweekoffs/{employeeId}")
	public List<EmpWeekOffEntity> getEmployeeWeekOffs(@PathVariable Integer employeeId) {
		return empWeekOffService.getEmployeeWeekOffs(employeeId);
	}

	@GetMapping("/monthlyweekoffs/{employeeId}/{year}/{month}")
	public Integer calculateMonthlyWeekOffs(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return empWeekOffService.calculateMonthlyWeekOffs(employeeId, year, month);
	}

	@PostMapping("/generatesundayweekoffs/{employeeId}/{year}/{month}")
	public String generateSundayWeekOffs(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return empWeekOffService.generateSundayWeekOffs(employeeId, year, month);
	}

	@DeleteMapping("/deleteempweekoff/{weekOffId}")
	public String deleteEmpWeekOff(@PathVariable Integer weekOffId) {
		return empWeekOffService.deleteEmpWeekOff(weekOffId);
	}
}
