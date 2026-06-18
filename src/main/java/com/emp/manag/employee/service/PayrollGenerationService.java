package com.emp.manag.employee.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.PayrollEntity;
import com.emp.manag.employee.entity.PayrollEntity.PayrollStatus;
import com.emp.manag.employee.entity.SalaryStructureEntity;
import com.emp.manag.employee.entity.TaxSlabEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.employee.repo.PayrollRepo;
import com.emp.manag.employee.repo.SalaryStructureRepo;
import com.emp.manag.schedule.entity.MonthlyAttendanceSummaryEntity;
import com.emp.manag.schedule.repo.MonthlyAttendanceSummaryRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class PayrollGenerationService {

    @Autowired
    private PayrollRepo payrollRepo;

    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private SalaryStructureRepo salaryRepo;

    @Autowired
    private MonthlyAttendanceSummaryRepo summaryRepo;

    public PayrollEntity generatePayroll(
            Integer employeeId,
            Integer year,
            Integer month) {

        EmpEntity employee = empRepo.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found"));

        SalaryStructureEntity salaryStructure =
                salaryRepo.findByEmployeeEmployeeid(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary Structure not found"));

        MonthlyAttendanceSummaryEntity summary =
                summaryRepo
                .findByEmployeeEmployeeidAndYearAndMonth(
                        employeeId,
                        year,
                        month)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Attendance Summary not found"));

        LocalDate payrollMonth =
                LocalDate.of(year, month, 1);

        PayrollEntity existingPayroll =
                payrollRepo
                .findByEmployeeEmployeeidAndPayrollMonth(
                        employeeId,
                        payrollMonth)
                .orElse(null);

        if (existingPayroll != null) {

            throw new RuntimeException(
                    "Payroll already generated for "
                    + payrollMonth);
        }

        TaxSlabEntity taxSlab =
                salaryStructure.getTaxSlab();

        /*
         * Attendance Calculation
         */

        double presentDays =
                summary.getPresentDays() == null
                        ? 0
                        : summary.getPresentDays();

        double halfDays =
                summary.getHalfDays() == null
                        ? 0
                        : summary.getHalfDays();

        double weekOffDays =
                summary.getWeekOffDays() == null
                        ? 0
                        : summary.getWeekOffDays();

        double publicHolidays =
                summary.getPublicHolidays() == null
                        ? 0
                        : summary.getPublicHolidays();

        double workingDays =
                summary.getWorkingDays() == null
                        ? 0
                        : summary.getWorkingDays();

        if (workingDays <= 0) {
            throw new RuntimeException(
                    "Monthly attendance summary is invalid. Working days cannot be zero.");
        }

        double paidDays =
                presentDays
                + (halfDays * 0.5)
                + weekOffDays
                + publicHolidays;

        /*
         * Production Validation
         */
        if (paidDays <= 0) {
            throw new RuntimeException(
                    "Attendance is not processed for employee "
                    + employeeId
                    + " for "
                    + month + "/" + year);
        }

        /*
         * Gross Salary
         */

        BigDecimal monthlyGross =
                salaryStructure.getBasicSalary()
                .add(salaryStructure.getHra())
                .add(salaryStructure.getAllowances());

        BigDecimal grossSalary =
                monthlyGross
                .multiply(BigDecimal.valueOf(paidDays))
                .divide(
                        BigDecimal.valueOf(workingDays),
                        2,
                        RoundingMode.HALF_UP);

        /*
         * Income Tax
         */

        BigDecimal incomeTax =
                grossSalary
                .multiply(taxSlab.getPercentage())
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP);

        /*
         * Total Deductions
         */

        BigDecimal deductions =
                salaryStructure.getPf()
                .add(salaryStructure.getEsi())
                .add(salaryStructure.getProfessionalTax())
                .add(incomeTax);

        /*
         * Net Salary
         */

        BigDecimal netSalary =
                grossSalary.subtract(deductions);

        if (netSalary.compareTo(BigDecimal.ZERO) < 0) {
            netSalary = BigDecimal.ZERO;
        }

        PayrollEntity payroll = new PayrollEntity();

        payroll.setEmployee(employee);

        payroll.setBasicSalary(
                salaryStructure.getBasicSalary());

        payroll.setHra(
                salaryStructure.getHra());

        payroll.setAllowances(
                salaryStructure.getAllowances());

        payroll.setBonus(BigDecimal.ZERO);

        payroll.setPf(
                salaryStructure.getPf());

        payroll.setEsi(
                salaryStructure.getEsi());

        payroll.setProfessionalTax(
                salaryStructure.getProfessionalTax());

        payroll.setIncomeTax(
                incomeTax);

        payroll.setDeductions(
                deductions);

        payroll.setGrossSalary(
                grossSalary);

        payroll.setNetSalary(
                netSalary);

        payroll.setTaxSlab(
                taxSlab);

        payroll.setPayrollMonth(
                payrollMonth);

        payroll.setApproved(false);

        payroll.setStatus(
                PayrollStatus.DRAFT);

        return payrollRepo.save(payroll);
    }
}