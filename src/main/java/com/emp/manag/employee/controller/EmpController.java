package com.emp.manag.employee.controller;

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

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.service.EmpService;

@RestController
@RequestMapping("/api/employee-management")
public class EmpController {
	
	@Autowired
	private EmpService service;

	@PostMapping("/Saveemp")
	public EmpEntity saveEmployee(@RequestBody EmpEntity employee) {
		System.out.println("===== REQUEST RECEIVED =====");
		System.out.println("Employee Data Saved");
		return service.saveEmployee(employee);
	}

	@PutMapping("/Update/{employeeId}")
	public String updateEmployee(@PathVariable Integer employeeId, @RequestBody EmpEntity employee) {
		System.out.println("Employee Data Updated");
		return service.updateEmployee(employeeId, employee);
	}

	@GetMapping("/GetAllEmp")
	public List<EmpEntity> displayEmployee() {
		return service.getAllEmployees();

	}

	@GetMapping("EmployeeById/{employeeId}")
	public EmpEntity getEmployeeById(@PathVariable Integer employeeId) {
		return service.getEmployeeById(employeeId);

	}

	@DeleteMapping("/DeleteEmp/{employeeId}")
	public String deleteEmployee(@PathVariable Integer employeeId) {
		return service.deleteEmployee(employeeId);
	}

	@DeleteMapping("/DeleteAllEmp")
	public String deleteAllEmployees() {
		return service.deleteAllEmployees();
	}


}
