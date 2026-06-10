package com.emp.manag.employee.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.PayrollEntity;
import com.emp.manag.employee.entity.PayrollEntity.PayrollStatus;
import com.emp.manag.employee.entity.TaxSlabEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.employee.repo.PayrollRepo;
import com.emp.manag.employee.repo.TaxSlabRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class PayrollService {

	@Autowired
	private PayrollRepo payrollRepo;

	@Autowired
	private EmpRepo empRepo;
	
	private TaxSlabRepo taxRepo;

	public PayrollEntity savePayroll(PayrollEntity payroll) {

		validatePayroll(payroll);
		attachEmployee(payroll);
		recalculatePayroll(payroll);
		
		Integer taxId = payroll.getTaxSlab().getTaxid();
		Integer employeeId = payroll.getEmployee().getEmployeeid();
		
		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
		TaxSlabEntity tax = taxRepo.findById(taxId)
				.orElseThrow(()-> new RuntimeException("Tax slab not found with ID: "  + taxId));
		
		payroll.setEmployee(employee);
		payroll.setTaxSlab(tax);
		
		if (payroll.getApproved() == null) {
			payroll.setApproved(false);
		}

		if (payroll.getStatus() == null) {
			payroll.setStatus(PayrollStatus.DRAFT);
		}

		return payrollRepo.save(payroll);
	}

	public PayrollEntity updatePayroll(Integer payrollId, PayrollEntity updatedPayroll) {

		if (payrollId == null) {
			throw new RuntimeException("Payroll ID is required");
		}

		validatePayroll(updatedPayroll);

		PayrollEntity existingPayroll = getPayrollById(payrollId);

		existingPayroll.setEmployee(updatedPayroll.getEmployee());
		existingPayroll.setBasicSalary(updatedPayroll.getBasicSalary());
		existingPayroll.setHra(updatedPayroll.getHra());
		existingPayroll.setAllowances(updatedPayroll.getAllowances());
		existingPayroll.setBonus(updatedPayroll.getBonus());
		existingPayroll.setDeductions(updatedPayroll.getDeductions());
		existingPayroll.setPf(updatedPayroll.getPf());
		existingPayroll.setEsi(updatedPayroll.getEsi());
		existingPayroll.setProfessionalTax(updatedPayroll.getProfessionalTax());
		existingPayroll.setIncomeTax(updatedPayroll.getIncomeTax());
		existingPayroll.setTaxSlab(updatedPayroll.getTaxSlab());
		existingPayroll.setPayrollMonth(updatedPayroll.getPayrollMonth());
		existingPayroll.setStatus(updatedPayroll.getStatus());
		existingPayroll.setApproved(updatedPayroll.getApproved());
		existingPayroll.setApprovedBy(updatedPayroll.getApprovedBy());

		attachEmployee(existingPayroll);
		recalculatePayroll(existingPayroll);

		return payrollRepo.save(existingPayroll);
	}

	public PayrollEntity approvePayroll(Integer payrollId, Integer approvedById) {

		if (approvedById == null) {
			throw new RuntimeException("Approver employee ID is required");
		}

		PayrollEntity payroll = getPayrollById(payrollId);
		EmpEntity approver = empRepo.findById(approvedById)
				.orElseThrow(() -> new RuntimeException("Approver not found with ID: " + approvedById));

		payroll.setApproved(true);
		payroll.setApprovedBy(approver);
		payroll.setStatus(PayrollStatus.HR_APPROVED);

		return payrollRepo.save(payroll);
	}

	public PayrollEntity getPayrollById(Integer payrollId) {

		if (payrollId == null) {
			throw new RuntimeException("Payroll ID is required");
		}

		return payrollRepo.findById(payrollId)
				.orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + payrollId));
	}

	public PayrollEntity getLatestPayrollByEmployee(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return payrollRepo.findTopByEmployeeEmployeeidOrderByPayrollMonthDesc(employeeId)
				.orElseThrow(() -> new RuntimeException("Payroll not found for employee ID: " + employeeId));
	}

	public List<PayrollEntity> getPayrollsByEmployee(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return payrollRepo.findByEmployeeEmployeeid(employeeId);
	}

	public List<PayrollEntity> getAllPayrolls() {
		return payrollRepo.findAll();
	}

	public String deletePayroll(Integer payrollId) {

		PayrollEntity payroll = getPayrollById(payrollId);

		if (payroll.getStatus() == PayrollStatus.PAID) {
			throw new RuntimeException("Paid payroll cannot be deleted");
		}

		payrollRepo.delete(payroll);

		return "Payroll deleted successfully";
	}
	
	public String deleteAllPayrolls() {
		 payrollRepo.deleteAll();
		 return "All payroll records deleted successfully";
	}

	private void validatePayroll(PayrollEntity payroll) {

		if (payroll == null) {
			throw new RuntimeException("Payroll details are required");
		}

		if (payroll.getEmployee() == null || payroll.getEmployee().getEmployeeid() == null) {
			throw new RuntimeException("Employee ID is required");
		}

		if (payroll.getPayrollMonth() == null) {
			throw new RuntimeException("Payroll month is required");
		}

		validateAmount(payroll.getBasicSalary(), "Basic salary");
		validateAmount(payroll.getHra(), "HRA");
		validateAmount(payroll.getAllowances(), "Allowances");
		validateAmount(payroll.getBonus(), "Bonus");
		validateAmount(payroll.getDeductions(), "Deductions");
		validateAmount(payroll.getPf(), "PF");
		validateAmount(payroll.getEsi(), "ESI");
		validateAmount(payroll.getProfessionalTax(), "Professional tax");
		validateAmount(payroll.getIncomeTax(), "Income tax");
	}

	private void validateAmount(BigDecimal amount, String fieldName) {

		if (amount == null) {
			throw new RuntimeException(fieldName + " is required");
		}

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException(fieldName + " cannot be negative");
		}
	}

	private void attachEmployee(PayrollEntity payroll) {

		Integer employeeId = payroll.getEmployee().getEmployeeid();
		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
		payroll.setEmployee(employee);
	}

	private void recalculatePayroll(PayrollEntity payroll) {

		BigDecimal grossSalary = payroll.getBasicSalary()
				.add(payroll.getHra())
				.add(payroll.getAllowances())
				.add(payroll.getBonus());

		BigDecimal totalDeductions = payroll.getDeductions()
				.add(payroll.getPf())
				.add(payroll.getEsi())
				.add(payroll.getProfessionalTax())
				.add(payroll.getIncomeTax());

		payroll.setGrossSalary(grossSalary);
		payroll.setNetSalary(grossSalary.subtract(totalDeductions));
	}
}
