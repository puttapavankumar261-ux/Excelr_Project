package com.emp.manag.setup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.EmpLoginEntity;
import com.emp.manag.employee.entity.EmpEntity.Department;
import com.emp.manag.employee.entity.EmpEntity.EmployeeRole;
import com.emp.manag.employee.entity.EmpEntity.EmploymentStatus;
import com.emp.manag.employee.entity.EmpEntity.EmploymentType;
import com.emp.manag.employee.entity.EmpEntity.JobLevel;
import com.emp.manag.employee.repo.EmpLoginRepo;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.setup.dto.AdminSetupRequest;

@Service
public class SetupService {

    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private EmpLoginRepo loginRepo;

    @Transactional
    public String createAdmin(AdminSetupRequest request) {

        validateRequest(request);

        String username = request.getUsername().trim();

        if (loginRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }

        EmpEntity employee = new EmpEntity();
        employee.setEmployeename(request.getEmployeeName().trim());
        employee.setRole(parseEnum(EmployeeRole.class, request.getRole(), EmployeeRole.ADMIN));
        employee.setDesignation(parseEnum(JobLevel.class, request.getDesignation(), JobLevel.MANAGER));
        employee.setDepartment(parseEnum(Department.class, request.getDepartment(), Department.ADMIN));
        employee.setEmploymentType(EmploymentType.FULL_TIME);
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        employee.setWorkLocation("Head Office");

        EmpEntity savedEmployee = empRepo.save(employee);

        EmpLoginEntity login = new EmpLoginEntity();
        login.setEmployee(savedEmployee);
        login.setUsername(username);
        login.setPasswordHash(request.getPassword());
        login.setRole(EmployeeRole.ADMIN.name());
        login.setStatus("ACTIVE");

        loginRepo.save(login);

        return "Admin account created successfully";
    }

    public boolean isInitialized() {

        return loginRepo.findAll().stream()
                .anyMatch(login -> "ADMIN".equalsIgnoreCase(login.getRole()));
    }

    private void validateRequest(AdminSetupRequest request) {

        if (request == null) {
            throw new RuntimeException("Admin setup details are required");
        }

        if (isBlank(request.getEmployeeName())) {
            throw new RuntimeException("Employee name is required");
        }

        if (isBlank(request.getUsername())) {
            throw new RuntimeException("Username is required");
        }

        if (isBlank(request.getPassword())) {
            throw new RuntimeException("Password is required");
        }
    }

    private boolean isBlank(String value) {

        return value == null || value.trim().isEmpty();
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value, T fallback) {

        if (isBlank(value)) {
            return fallback;
        }

        try {
            return Enum.valueOf(enumType, value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }
}
