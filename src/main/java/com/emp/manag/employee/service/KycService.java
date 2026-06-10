package com.emp.manag.employee.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.KycEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.employee.repo.KycRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class KycService {

	@Autowired
	private KycRepo kycRepo;

	@Autowired
	private EmpRepo empRepo;

	public KycEntity saveKyc(KycEntity kyc) {

		validateKyc(kyc);
		
		EmpEntity verifier = empRepo.findById(
		        kyc.getVerifiedBy().getEmployeeid())
		        .orElseThrow(() ->
		            new RuntimeException("Verifier not found"));

		kyc.setVerifiedBy(verifier);

		Integer employeeId = kyc.getEmployee().getEmployeeid();

		if (kycRepo.existsByEmployeeEmployeeid(employeeId)) {
			throw new RuntimeException("KYC already exists for employee ID: " + employeeId);
		}

		if (kyc.getAadhaarNumber() != null && kycRepo.existsByAadhaarNumber(kyc.getAadhaarNumber())) {
			throw new RuntimeException("Aadhaar number already exists");
		}

		if (kyc.getPanNumber() != null && kycRepo.existsByPanNumber(kyc.getPanNumber())) {
			throw new RuntimeException("PAN number already exists");
		}

		kyc.setEmployee(findEmployee(employeeId, "Employee"));

		if (kyc.getVerified() == null) {
			kyc.setVerified(false);
		}

		return kycRepo.save(kyc);
	}

	public KycEntity updateKyc(Integer kycId, KycEntity updatedKyc) {

		if (kycId == null) {
			throw new RuntimeException("KYC ID is required");
		}

		validateKyc(updatedKyc);

		KycEntity existingKyc = getKycById(kycId);
		existingKyc.setEmployee(findEmployee(updatedKyc.getEmployee().getEmployeeid(), "Employee"));
		existingKyc.setAadhaarNumber(updatedKyc.getAadhaarNumber());
		existingKyc.setPanNumber(updatedKyc.getPanNumber());
		existingKyc.setBankAccountNumber(updatedKyc.getBankAccountNumber());
		existingKyc.setIfscCode(updatedKyc.getIfscCode());
		existingKyc.setBankName(updatedKyc.getBankName());

		return kycRepo.save(existingKyc);
	}

	public KycEntity verifyKyc(Integer kycId, Integer verifiedById) {

		if (verifiedById == null) {
			throw new RuntimeException("Verifier employee ID is required");
		}

		KycEntity kyc = getKycById(kycId);
		kyc.setVerified(true);
		kyc.setVerifiedBy(findEmployee(verifiedById, "Verifier"));
		kyc.setVerifiedOn(LocalDateTime.now());

		return kycRepo.save(kyc);
	}

	public KycEntity getKycById(Integer kycId) {

		if (kycId == null) {
			throw new RuntimeException("KYC ID is required");
		}

		return kycRepo.findById(kycId)
				.orElseThrow(() -> new RuntimeException("KYC not found with ID: " + kycId));
	}

	public KycEntity getKycByEmployee(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return kycRepo.findByEmployeeEmployeeid(employeeId)
				.orElseThrow(() -> new RuntimeException("KYC not found for employee ID: " + employeeId));
	}

	public List<KycEntity> getAllKycs() {
		return kycRepo.findAll();
	}

	public String deleteKyc(Integer kycId) {

		KycEntity kyc = getKycById(kycId);
		kycRepo.delete(kyc);

		return "KYC deleted successfully";
	}
	
	public String deleteAllKyc() {
		kycRepo.deleteAll();
		return "All KYC records deleted successfully";
	}

	private void validateKyc(KycEntity kyc) {

		if (kyc == null) {
			throw new RuntimeException("KYC details are required");
		}

		if (kyc.getEmployee() == null || kyc.getEmployee().getEmployeeid() == null) {
			throw new RuntimeException("Employee ID is required");
		}

		if (kyc.getPanNumber() != null && !kyc.getPanNumber().matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
			throw new RuntimeException("Invalid PAN number format");
		}

		if (kyc.getAadhaarNumber() != null && !kyc.getAadhaarNumber().matches("[0-9]{12}")) {
			throw new RuntimeException("Aadhaar number must contain 12 digits");
		}
	}

	private EmpEntity findEmployee(Integer employeeId, String label) {
		return empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException(label + " not found with ID: " + employeeId));
	}
}
