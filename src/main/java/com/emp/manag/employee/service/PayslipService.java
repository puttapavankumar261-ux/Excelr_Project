package com.emp.manag.employee.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.PayrollEntity;
import com.emp.manag.employee.entity.PayslipEntity;
import com.emp.manag.employee.entity.PayslipEntity.PayslipStatus;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.employee.repo.PayrollRepo;
import com.emp.manag.employee.repo.PayslipRepo;
import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;
import com.emp.manag.schedule.service.MonthlyAttendanceSummaryService;

@Service
@Transactional(rollbackFor = Exception.class)
public class PayslipService {

	@Autowired
	private PayslipRepo payslipRepo;

	@Autowired
	private PayrollRepo payrollRepo;

	@Autowired
	private EmpRepo empRepo;

	@Autowired
	private MonthlyAttendanceSummaryService monthlySummaryService;

	public PayslipEntity generatePayslip(Integer employeeId, Integer year, Integer month) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		YearMonth yearMonth = validateYearMonth(year, month);

		if (payslipRepo.existsByEmployeeEmployeeidAndYearAndMonth(employeeId, year, month)) {
			throw new RuntimeException("Payslip already exists for employee/month");
		}

		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		PayrollEntity payroll = payrollRepo.findTopByEmployeeEmployeeidOrderByPayrollMonthDesc(employeeId)
				.orElseThrow(() -> new RuntimeException("Payroll not found for employee ID: " + employeeId));

		MonthlyAttendanceSummaryEntity summary = monthlySummaryService.generateMonthlySummary(employeeId, year, month);

		PayslipEntity payslip = new PayslipEntity();
		payslip.setEmployee(employee);
		payslip.setPayroll(payroll);
		payslip.setMonthlySummary(summary);
		payslip.setYear(year);
		payslip.setMonth(month);
		payslip.setPayPeriodStart(yearMonth.atDay(1));
		payslip.setPayPeriodEnd(yearMonth.atEndOfMonth());
		recalculatePayslip(payslip, payroll, summary);
		payslip.setStatus(PayslipStatus.GENERATED);

		return payslipRepo.save(payslip);
	}

	public PayslipEntity recalculatePayslip(Integer payslipId) {

		PayslipEntity payslip = getPayslipById(payslipId);

		if (payslip.getStatus() == PayslipStatus.PAID) {
			throw new RuntimeException("Paid payslip cannot be recalculated");
		}

		recalculatePayslip(payslip, payslip.getPayroll(), payslip.getMonthlySummary());

		return payslipRepo.save(payslip);
	}

	public PayslipEntity approvePayslip(Integer payslipId, Integer approvedById) {

		if (approvedById == null) {
			throw new RuntimeException("Approver employee ID is required");
		}

		PayslipEntity payslip = getPayslipById(payslipId);
		EmpEntity approver = empRepo.findById(approvedById)
				.orElseThrow(() -> new RuntimeException("Approver not found with ID: " + approvedById));

		payslip.setApprovedBy(approver);
		payslip.setApprovedOn(LocalDateTime.now());
		payslip.setStatus(PayslipStatus.HR_APPROVED);

		return payslipRepo.save(payslip);
	}

	public PayslipEntity markPayslipPaid(Integer payslipId) {

		PayslipEntity payslip = getPayslipById(payslipId);

		if (payslip.getStatus() != PayslipStatus.HR_APPROVED
				&& payslip.getStatus() != PayslipStatus.FINANCE_APPROVED) {
			throw new RuntimeException("Payslip must be approved before marking paid");
		}

		payslip.setStatus(PayslipStatus.PAID);
		payslip.setPaidOn(LocalDate.now());

		return payslipRepo.save(payslip);
	}

	public PayslipEntity getPayslipById(Integer payslipId) {

		if (payslipId == null) {
			throw new RuntimeException("Payslip ID is required");
		}

		return payslipRepo.findById(payslipId)
				.orElseThrow(() -> new RuntimeException("Payslip not found with ID: " + payslipId));
	}

	public PayslipEntity getPayslipByEmployeeMonth(Integer employeeId, Integer year, Integer month) {

		validateYearMonth(year, month);

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return payslipRepo.findByEmployeeEmployeeidAndYearAndMonth(employeeId, year, month)
				.orElseThrow(() -> new RuntimeException("Payslip not found for employee/month"));
	}

	public List<PayslipEntity> getPayslipsByEmployee(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return payslipRepo.findByEmployeeEmployeeid(employeeId);
	}

	public List<PayslipEntity> getAllPayslips() {
		return payslipRepo.findAll();
	}

	public String deletePayslip(Integer payslipId) {

		PayslipEntity payslip = getPayslipById(payslipId);

		if (payslip.getStatus() == PayslipStatus.PAID) {
			throw new RuntimeException("Paid payslip cannot be deleted");
		}

		payslipRepo.delete(payslip);

		return "Payslip deleted successfully";
	}

	public String deleteAllPayslips() {
		payslipRepo.deleteAll();
		return "All Payslips records deleted successfully";
	}
	
	private void recalculatePayslip(PayslipEntity payslip, PayrollEntity payroll,
			MonthlyAttendanceSummaryEntity summary) {

		int workingDays = summary.getWorkingDays() == null || summary.getWorkingDays() == 0
				? payslip.getPayPeriodEnd().lengthOfMonth()
				: summary.getWorkingDays();

		BigDecimal absentDays = BigDecimal.valueOf(summary.getAbsentDays() == null ? 0 : summary.getAbsentDays());
		BigDecimal halfDays = BigDecimal.valueOf(summary.getHalfDays() == null ? 0 : summary.getHalfDays())
				.multiply(BigDecimal.valueOf(0.5));
		BigDecimal lopDays = absentDays.add(halfDays);
		BigDecimal paidDays = BigDecimal.valueOf(workingDays).subtract(lopDays).max(BigDecimal.ZERO);

		BigDecimal monthlyGross = payroll.getBasicSalary()
				.add(payroll.getHra())
				.add(payroll.getAllowances())
				.add(payroll.getBonus());
		BigDecimal perDaySalary = monthlyGross.divide(BigDecimal.valueOf(workingDays), 2, RoundingMode.HALF_UP);
		BigDecimal lopDeduction = perDaySalary.multiply(lopDays).setScale(2, RoundingMode.HALF_UP);

		BigDecimal totalDeductions = lopDeduction
				.add(payroll.getDeductions())
				.add(payroll.getPf())
				.add(payroll.getEsi())
				.add(payroll.getProfessionalTax())
				.add(payroll.getIncomeTax());

		payslip.setWorkingDays(workingDays);
		payslip.setPaidDays(paidDays);
		payslip.setLopDays(lopDays);
		payslip.setBasicSalary(payroll.getBasicSalary());
		payslip.setHra(payroll.getHra());
		payslip.setAllowances(payroll.getAllowances());
		payslip.setBonus(payroll.getBonus());
		payslip.setGrossEarnings(monthlyGross);
		payslip.setLopDeduction(lopDeduction);
		payslip.setPf(payroll.getPf());
		payslip.setEsi(payroll.getEsi());
		payslip.setProfessionalTax(payroll.getProfessionalTax());
		payslip.setIncomeTax(payroll.getIncomeTax());
		payslip.setOtherDeductions(payroll.getDeductions());
		payslip.setTotalDeductions(totalDeductions);
		payslip.setNetPay(monthlyGross.subtract(totalDeductions).max(BigDecimal.ZERO));
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
