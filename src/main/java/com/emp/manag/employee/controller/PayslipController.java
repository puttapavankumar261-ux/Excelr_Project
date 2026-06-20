package com.emp.manag.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.employee.entity.PayslipEntity;
import com.emp.manag.employee.service.PayslipPdfService;
import com.emp.manag.employee.service.PayslipService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/employee-management")
public class PayslipController {

	@Autowired
	private PayslipService payslipService;
	
	@Autowired
	private PayslipPdfService pdfService;

	@PostMapping("/generatepayslip/{employeeId}/{year}/{month}")
	public PayslipEntity generatePayslip(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return payslipService.generatePayslip(employeeId, year, month);
	}

	@PutMapping("/recalculatepayslip/{payslipId}")
	public PayslipEntity recalculatePayslip(@PathVariable Integer payslipId) {
		return payslipService.recalculatePayslip(payslipId);
	}

	@PutMapping("/approvepayslip/{payslipId}/{approvedById}")
	public PayslipEntity approvePayslip(@PathVariable Integer payslipId, @PathVariable Integer approvedById) {
		return payslipService.approvePayslip(payslipId, approvedById);
	}

	@PutMapping("/markpayslippaid/{payslipId}")
	public PayslipEntity markPayslipPaid(@PathVariable Integer payslipId) {
		return payslipService.markPayslipPaid(payslipId);
	}

	@GetMapping("/getpayslip/{payslipId}")
	public PayslipEntity getPayslipById(@PathVariable Integer payslipId) {
		return payslipService.getPayslipById(payslipId);
	}

	@GetMapping("/getemployeepayslip/{employeeId}/{year}/{month}")
	public PayslipEntity getPayslipByEmployeeMonth(@PathVariable Integer employeeId,
			@PathVariable Integer year, @PathVariable Integer month) {
		return payslipService.getPayslipByEmployeeMonth(employeeId, year, month);
	}

	@GetMapping("/getemployeepayslips/{employeeId}")
	public List<PayslipEntity> getPayslipsByEmployee(@PathVariable Integer employeeId) {
		return payslipService.getPayslipsByEmployee(employeeId);
	}

	@GetMapping("/getallpayslips")
	public List<PayslipEntity> getAllPayslips() {
		return payslipService.getAllPayslips();
	}
	
	@GetMapping("/downloadpayslip/{payslipId}")
	public ResponseEntity<byte[]>
	downloadPayslip(
	        @PathVariable Integer payslipId) {

	    byte[] pdf =
	            pdfService.generatePdf(
	                    payslipId);

	    return ResponseEntity.ok()
	            .header(
	                    HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=payslip.pdf")
	            .contentType(
	                    MediaType.APPLICATION_PDF)
	            .body(pdf);
	}

	@DeleteMapping("/deletepayslip/{payslipId}")
	public String deletePayslip(@PathVariable Integer payslipId) {
		return payslipService.deletePayslip(payslipId);
	}
	
	
	@DeleteMapping("/DeleteAllPayslips")
	public String deleteAllPayslips() {
		return payslipService.deleteAllPayslips();
	}
	
}
