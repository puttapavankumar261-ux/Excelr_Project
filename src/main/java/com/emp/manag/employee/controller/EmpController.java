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

import com.emp.manag.config.dto.EmployeeOnboardingRequest;
import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.service.EmpService;
import com.emp.manag.schedule.entity.ShiftEntity;

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
	
	@PostMapping("/onboardemployee")
	public String onboardEmployee(
	        @RequestBody EmployeeOnboardingRequest request) {

	    return service.onboardEmployee(request);
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

	@GetMapping("/EmployeeById/{employeeId}")
	public EmpEntity getEmployeeById(@PathVariable Integer employeeId) {
	    return service.getEmployeeById(employeeId);
	}
	
	@GetMapping(
		    "/employeeshift/{employeeId}")
		public ShiftEntity getEmployeeShift(
		        @PathVariable Integer employeeId) {

		    EmpEntity employee =
		            service.getEmployeeById(employeeId);

		    return employee.getShift();
		}
	
	@PutMapping(
		    "/assignshift/{employeeId}/{shiftId}")
		public EmpEntity assignShift(
		        @PathVariable Integer employeeId,
		        @PathVariable Integer shiftId) {

		return service.assignShift(
		        employeeId,
		        shiftId);
		}

	@DeleteMapping("/DeleteEmp/{employeeId}")
	public String deleteEmployee(
	        @PathVariable Integer employeeId) {

	    System.out.println(
	            "DELETE REQUEST FOR EMPLOYEE : "
	            + employeeId);

	    try {

	        return service.deleteEmployee(
	                employeeId);

	    } catch (Exception e) {

	        e.printStackTrace();

	        throw e;
	    }
	}
	@DeleteMapping("/DeleteAllEmp")
	public String deleteAllEmployees() {
		return service.deleteAllEmployees();
	}


}
