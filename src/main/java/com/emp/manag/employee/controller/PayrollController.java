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

import com.emp.manag.employee.entity.PayrollEntity;
import com.emp.manag.employee.service.PayrollService;

@RestController
@RequestMapping("/api/employee-management")
public class PayrollController {

	@Autowired
	private PayrollService payrollService;

	@PostMapping("/savepayroll")
	public PayrollEntity savePayroll(@RequestBody PayrollEntity payroll) {
		return payrollService.savePayroll(payroll);
	}

	@PutMapping("/updatepayroll/{payrollId}")
	public PayrollEntity updatePayroll(@PathVariable Integer payrollId, @RequestBody PayrollEntity payroll) {
		return payrollService.updatePayroll(payrollId, payroll);
	}

	@PutMapping("/approvepayroll/{payrollId}/{approvedById}")
	public PayrollEntity approvePayroll(@PathVariable Integer payrollId, @PathVariable Integer approvedById) {
		return payrollService.approvePayroll(payrollId, approvedById);
	}

	@GetMapping("/getpayroll/{payrollId}")
	public PayrollEntity getPayrollById(@PathVariable Integer payrollId) {
		return payrollService.getPayrollById(payrollId);
	}

	@GetMapping("/getlatestpayroll/{employeeId}")
	public PayrollEntity getLatestPayrollByEmployee(@PathVariable Integer employeeId) {
		return payrollService.getLatestPayrollByEmployee(employeeId);
	}

	@GetMapping("/getemployeepayrolls/{employeeId}")
	public List<PayrollEntity> getPayrollsByEmployee(@PathVariable Integer employeeId) {
		return payrollService.getPayrollsByEmployee(employeeId);
	}

	@GetMapping("/getallpayrolls")
	public List<PayrollEntity> getAllPayrolls() {
		return payrollService.getAllPayrolls();
	}

	@DeleteMapping("/deletepayroll/{payrollId}")
	public String deletePayroll(@PathVariable Integer payrollId) {
		return payrollService.deletePayroll(payrollId);
	}
	
	@DeleteMapping("/DeleteAllPayrolls")
	public String deleteAllPayroll() {
	 return	payrollService.deleteAllPayrolls();
	}
}
