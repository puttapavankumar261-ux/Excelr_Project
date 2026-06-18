package com.emp.manag.employee.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.config.dto.EmployeeOnboardingRequest;
import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.EmpEntity.EmploymentStatus;
import com.emp.manag.employee.entity.EmpLoginEntity;
import com.emp.manag.employee.repo.EmpLoginRepo;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.schedule.entity.ShiftEntity;
import com.emp.manag.schedule.repo.ShiftRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmpService {

	@Autowired
	private EmpRepo empRepo;
	
	@Autowired
	private ShiftRepo shiftRepo;

	@Autowired
	private EmpLoginRepo loginRepo;
	
	
	
	public EmpEntity saveEmployee(EmpEntity employee) {

	    validateEmployee(employee);
	    validateManagerHierarchy(employee.getEmployeeid(), employee.getManager());
	    attachEmployeeRelations(employee);

	    if (employee.getEmploymentStatus() == null) {
	        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
	    }

	    return empRepo.save(employee);
	}

	public String updateEmployee(Integer employeeId, EmpEntity updatedEmployee) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		validateEmployee(updatedEmployee);

		EmpEntity existingEmployee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		validateManagerHierarchy(employeeId, updatedEmployee.getManager());

		existingEmployee.setManager(updatedEmployee.getManager());
		existingEmployee.setShift(updatedEmployee.getShift());
		attachEmployeeRelations(existingEmployee);

		existingEmployee.setEmployeename(updatedEmployee.getEmployeename());
		existingEmployee.setCompanyemail(updatedEmployee.getCompanyemail());
		existingEmployee.setPhonenumber(updatedEmployee.getPhonenumber());
		existingEmployee.setImage(updatedEmployee.getImage());		
		existingEmployee.setRole(updatedEmployee.getRole());
		existingEmployee.setJoiningDate(updatedEmployee.getJoiningDate());
		existingEmployee.setResignationDate(updatedEmployee.getResignationDate());
		existingEmployee.setDesignation(updatedEmployee.getDesignation());
		existingEmployee.setDepartment(updatedEmployee.getDepartment());
		existingEmployee.setEmploymentType(updatedEmployee.getEmploymentType());
		existingEmployee.setEmploymentStatus(updatedEmployee.getEmploymentStatus());
		existingEmployee.setWorkLocation(updatedEmployee.getWorkLocation());
		existingEmployee.setBankName(
		        updatedEmployee.getBankName());

		existingEmployee.setAccountHolderName(
		        updatedEmployee.getAccountHolderName());

		existingEmployee.setAccountNumber(
		        updatedEmployee.getAccountNumber());

		existingEmployee.setIfscCode(
		        updatedEmployee.getIfscCode());
		existingEmployee.setBasicSalary(
		        updatedEmployee.getBasicSalary());

		existingEmployee.setHra(
		        updatedEmployee.getHra());

		existingEmployee.setAllowances(
		        updatedEmployee.getAllowances());
		empRepo.save(existingEmployee);
		return "Employee record updated successfully";
	}

	public List<EmpEntity> getAllEmployees() {
		return empRepo.findAll();
	}

	public EmpEntity getEmployeeById(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
	}

	@Transactional
	public String deleteEmployee(Integer employeeId) {

	    try {

	        EmpEntity employee = empRepo.findById(employeeId)
	                .orElseThrow(() ->
	                        new RuntimeException("Employee not found"));

	        System.out.println("Deleting Employee ID = " + employeeId);

	        empRepo.delete(employee);

	        empRepo.flush();

	        System.out.println("Delete Successful");

	        return "Employee deleted successfully";

	    } catch (Exception e) {

	        e.printStackTrace();

	        throw new RuntimeException(
	                "DELETE FAILED -> "
	                + e.getClass().getName()
	                + " -> "
	                + e.getMessage());
	    }
	}

	public String deleteAllEmployees() {
		empRepo.deleteAll();
		return "All employee records deleted successfully";
	}
	
	public String onboardEmployee(
	        EmployeeOnboardingRequest request) {

	    EmpEntity employee = request.getEmployee();

	    validateEmployee(employee);

	    attachEmployeeRelations(employee);

	    // Save Employee
	    EmpEntity savedEmployee = empRepo.save(employee);

	    // Create Login Automatically
	    EmpLoginEntity login = new EmpLoginEntity();

	    login.setEmployee(savedEmployee);

	    // Username = Company Email
	    login.setUsername(
	            savedEmployee.getCompanyemail());

	    // Default Password = Company Email
	    login.setPasswordHash(
	            savedEmployee.getCompanyemail());

	    // Role from Employee
	    login.setRole(
	            savedEmployee.getRole().name());

	    // Active by default
	    login.setStatus("ACTIVE");

	    // Save Login
	    loginRepo.save(login);

	    return "Employee onboarded successfully. Username and password created automatically.";
	}

	private void validateEmployee(EmpEntity employee) {

		if (employee == null) {
			throw new RuntimeException("Employee details are required");
		}
		
		if (employee.getRole() == null) {
			throw new RuntimeException("Employee role is required");
		}

		if (employee.getDesignation() == null) {
			throw new RuntimeException("Employee designation is required");
		}

		if (employee.getDepartment() == null) {
			throw new RuntimeException("Employee department is required");
		}

		if (employee.getEmploymentType() == null) {
			throw new RuntimeException("Employee employment type is required");
		}

		if (employee.getJoiningDate() != null && employee.getJoiningDate().isAfter(LocalDate.now())) {
			throw new RuntimeException("Joining date cannot be in the future");
		}

		if (employee.getResignationDate() != null && employee.getJoiningDate() != null
				&& employee.getResignationDate().isBefore(employee.getJoiningDate())) {
			throw new RuntimeException("Resignation date cannot be before joining date");
		}
	}

	private void attachEmployeeRelations(EmpEntity employee) {

		if (employee.getManager() != null && employee.getManager().getEmployeeid() != null) {
			Integer managerId = employee.getManager().getEmployeeid();
			EmpEntity manager = empRepo.findById(managerId)
					.orElseThrow(() -> new RuntimeException("Manager not found with ID: " + managerId));
			employee.setManager(manager);
		}

		if (employee.getShift() != null && employee.getShift().getShiftid() != null) {
			Integer shiftId = employee.getShift().getShiftid();
			ShiftEntity shift = shiftRepo.findById(shiftId)
					.orElseThrow(() -> new RuntimeException("Shift not found with ID: " + shiftId));
			employee.setShift(shift);
		}
	}

	private void validateManagerHierarchy(Integer employeeId, EmpEntity manager) {

		if (manager == null || manager.getEmployeeid() == null || employeeId == null) {
			return;
		}

		Integer managerId = manager.getEmployeeid();

		if (employeeId.equals(managerId)) {
			throw new RuntimeException("Employee cannot be their own manager");
		}

		Set<Integer> visitedManagerIds = new HashSet<>();
		Integer currentManagerId = managerId;

		while (currentManagerId != null) {
			if (!visitedManagerIds.add(currentManagerId)) {
				throw new RuntimeException("Circular manager hierarchy detected");
			}

			if (employeeId.equals(currentManagerId)) {
				throw new RuntimeException("Circular manager hierarchy detected: employee cannot report to a subordinate");
			}

			Integer lookupManagerId = currentManagerId;
			EmpEntity currentManager = empRepo.findById(lookupManagerId)
					.orElseThrow(() -> new RuntimeException("Manager not found with ID: " + lookupManagerId));

			currentManagerId = currentManager.getManager() == null ? null
					: currentManager.getManager().getEmployeeid();
		}
	}
}
