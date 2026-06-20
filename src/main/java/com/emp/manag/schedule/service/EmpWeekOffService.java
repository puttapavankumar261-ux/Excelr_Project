package com.emp.manag.schedule.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.schedule.entity.EmpWeekOffEntity;
import com.emp.manag.schedule.entity.WeekOffPolicyEntity;
import com.emp.manag.schedule.repo.EmpWeekOffRepo;
import com.emp.manag.schedule.repo.WeekOffPolicyRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmpWeekOffService {

    @Autowired
    private EmpWeekOffRepo empWeekOffRepo;

    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private WeekOffPolicyRepo weekOffPolicyRepo;

    public EmpWeekOffEntity saveEmpWeekOff(EmpWeekOffEntity empWeekOff) {

        System.out.println("=================================");
        System.out.println("Received WeekOff Request");
        System.out.println("WeekOff = " + empWeekOff);

        if (empWeekOff != null) {
            System.out.println("Employee Object = " + empWeekOff.getEmployee());

            if (empWeekOff.getEmployee() != null) {
                System.out.println("Employee ID = "
                        + empWeekOff.getEmployee().getEmployeeid());
            }
        }
        System.out.println("=================================");

        validateEmpWeekOff(empWeekOff);

        Integer employeeId = empWeekOff.getEmployee().getEmployeeid();

        EmpEntity employee = empRepo.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with ID: "
                                        + employeeId));

        if (empWeekOffRepo.existsByEmployeeEmployeeidAndWeekOffDate(
                employeeId,
                empWeekOff.getWeekOffDate())) {

            throw new RuntimeException(
                    "Week off already exists for this employee on this date");
        }

        empWeekOff.setEmployee(employee);

        if (empWeekOff.getWeekOffPolicy() != null
                && empWeekOff.getWeekOffPolicy().getPolicyId() != null) {

            Integer policyId =
                    empWeekOff.getWeekOffPolicy().getPolicyId();

            WeekOffPolicyEntity policy =
                    weekOffPolicyRepo.findById(policyId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Week off policy not found with ID: "
                                                    + policyId));

            empWeekOff.setWeekOffPolicy(policy);
        }

        return empWeekOffRepo.save(empWeekOff);
    }

    public EmpWeekOffEntity updateEmpWeekOff(Integer weekOffId, EmpWeekOffEntity updatedWeekOff) {

        if (weekOffId == null) {
            throw new RuntimeException("Week off ID is required");
        }

        validateEmpWeekOff(updatedWeekOff);

        EmpWeekOffEntity existingWeekOff = empWeekOffRepo.findById(weekOffId)
                .orElseThrow(() -> new RuntimeException("Week off record not found with ID: " + weekOffId));

        Integer employeeId = updatedWeekOff.getEmployee().getEmployeeid();

        EmpEntity employee = empRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        boolean duplicateExists = empWeekOffRepo.existsByEmployeeEmployeeidAndWeekOffDate(
                employeeId, updatedWeekOff.getWeekOffDate());

        if (duplicateExists &&
                !existingWeekOff.getWeekOffDate().equals(updatedWeekOff.getWeekOffDate())) {
            throw new RuntimeException("Week off already exists for this employee on this date");
        }

        existingWeekOff.setEmployee(employee);
        existingWeekOff.setWeekOffDate(updatedWeekOff.getWeekOffDate());

        if (updatedWeekOff.getWeekOffPolicy() != null &&
                updatedWeekOff.getWeekOffPolicy().getPolicyId() != null) {

            Integer policyId = updatedWeekOff.getWeekOffPolicy().getPolicyId();

            WeekOffPolicyEntity policy = weekOffPolicyRepo.findById(policyId)
                    .orElseThrow(() -> new RuntimeException("Week off policy not found with ID: " + policyId));

            existingWeekOff.setWeekOffPolicy(policy);
        }

        return empWeekOffRepo.save(existingWeekOff);
    }

    public EmpWeekOffEntity getEmpWeekOffById(Integer weekOffId) {

        if (weekOffId == null) {
            throw new RuntimeException("Week off ID is required");
        }

        return empWeekOffRepo.findById(weekOffId)
                .orElseThrow(() -> new RuntimeException("Week off record not found with ID: " + weekOffId));
    }

    public List<EmpWeekOffEntity> getAllEmpWeekOffs() {
        return empWeekOffRepo.findAll();
    }

    public List<EmpWeekOffEntity> getEmployeeWeekOffs(Integer employeeId) {

        if (employeeId == null) {
            throw new RuntimeException("Employee ID is required");
        }

        return empWeekOffRepo.findByEmployeeEmployeeid(employeeId);
    }

    public String deleteEmpWeekOff(Integer weekOffId) {

        if (weekOffId == null) {
            throw new RuntimeException("Week off ID is required");
        }

        EmpWeekOffEntity existingWeekOff = empWeekOffRepo.findById(weekOffId)
                .orElseThrow(() -> new RuntimeException("Week off record not found with ID: " + weekOffId));

        empWeekOffRepo.delete(existingWeekOff);

        return "Employee week off deleted successfully";
    }
    
    public String deleteAllWeekOffs(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		List<EmpWeekOffEntity> weekOffs = empWeekOffRepo.findByEmployeeEmployeeid(employeeId);

		if (weekOffs.isEmpty()) {
			return "No week off records found for this employee";
		}

		empWeekOffRepo.deleteAll(weekOffs);

		return "All week off records for the employee deleted successfully";
	}

    public Integer calculateMonthlyWeekOffs(Integer employeeId, Integer year, Integer month) {

        if (employeeId == null) {
            throw new RuntimeException("Employee ID is required");
        }

        if (year == null || month == null) {
            throw new RuntimeException("Year and month are required");
        }

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return empWeekOffRepo.countByEmployeeEmployeeidAndWeekOffDateBetween(
                employeeId, startDate, endDate);
    }

    public String generateSundayWeekOffs(Integer employeeId, Integer year, Integer month) {

        if (employeeId == null) {
            throw new RuntimeException("Employee ID is required");
        }

        if (year == null || month == null) {
            throw new RuntimeException("Year and month are required");
        }

        EmpEntity employee = empRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        int createdCount = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            if (date.getDayOfWeek() == DayOfWeek.SUNDAY &&
                    !empWeekOffRepo.existsByEmployeeEmployeeidAndWeekOffDate(employeeId, date)) {

                EmpWeekOffEntity weekOff = new EmpWeekOffEntity();
                weekOff.setEmployee(employee);
                weekOff.setWeekOffDate(date);

                empWeekOffRepo.save(weekOff);
                createdCount++;
            }
        }

        return createdCount + " Sunday week offs generated successfully";
    }

    private void validateEmpWeekOff(EmpWeekOffEntity empWeekOff) {

        if (empWeekOff == null) {
            throw new RuntimeException("Week off details are required");
        }

        if (empWeekOff.getEmployee() == null ||
                empWeekOff.getEmployee().getEmployeeid() == null) {
            throw new RuntimeException("Employee ID is required");
        }

        if (empWeekOff.getWeekOffDate() == null) {
            throw new RuntimeException("Week off date is required");
        }
    }
}