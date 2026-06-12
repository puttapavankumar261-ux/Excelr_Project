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

import com.emp.manag.employee.entity.KycEntity;
import com.emp.manag.employee.service.KycService;

@RestController
@RequestMapping("/api/employee-management")
public class KycController {

	@Autowired
	private KycService kycService;

	@PostMapping("/savekyc")
	public KycEntity saveKyc(@RequestBody KycEntity kyc) {
		return kycService.saveKyc(kyc);
	}

	@PutMapping("/updatekyc/{kycId}")
	public KycEntity updateKyc(@PathVariable Integer kycId, @RequestBody KycEntity kyc) {
		return kycService.updateKyc(kycId, kyc);
	}

	@PutMapping("/verifykyc/{kycId}/{verifiedById}")
	public KycEntity verifyKyc(@PathVariable Integer kycId, @PathVariable Integer verifiedById) {
		return kycService.verifyKyc(kycId, verifiedById);
	}

	@GetMapping("/getkyc/{kycId}")
	public KycEntity getKycById(@PathVariable Integer kycId) {
		return kycService.getKycById(kycId);
	}

	@GetMapping("/getemployeekyc/{employeeId}")
	public KycEntity getKycByEmployee(@PathVariable Integer employeeId) {
		return kycService.getKycByEmployee(employeeId);
	}

	@GetMapping("/getallkycs")
	public List<KycEntity> getAllKycs() {
		return kycService.getAllKycs();
	}

	@DeleteMapping("/deletekyc/{kycId}")
	public String deleteKyc(@PathVariable Integer kycId) {
		return kycService.deleteKyc(kycId);
	}
	
	@DeleteMapping("/DeleteAllKyc")
	public String deleteAllKyc(){
		return kycService.deleteAllKyc();
		
	}
}
