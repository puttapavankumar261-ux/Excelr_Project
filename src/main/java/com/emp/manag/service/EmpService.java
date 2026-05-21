package com.emp.manag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.manag.entity.EmpEntity;
import com.emp.manag.repo.EmpRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class EmpService {
	
	@Autowired
	private EmpRepo Repo;

	public EmpEntity saveEmployee(EmpEntity employee) {

		return Repo.save(employee);
	}

	public String updateEmployee(Integer employeeid, EmpEntity updatedEmployee) {
		EmpEntity existEmp = Repo.findById(employeeid)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		existEmp.setManager(updatedEmployee.getManager());
		existEmp.setEmployeeName(updatedEmployee.getEmployeeName());
		existEmp.setJoiningDate(updatedEmployee.getJoiningDate());
		existEmp.setResignationDate(updatedEmployee.getResignationDate());
		existEmp.setDesignation(updatedEmployee.getDesignation());
		existEmp.setDepartment(updatedEmployee.getDepartment());
		existEmp.setEmploymentType(updatedEmployee.getEmploymentType());
		existEmp.setEmploymentStatus(updatedEmployee.getEmploymentStatus());
		existEmp.setWorkLocation(updatedEmployee.getWorkLocation());

		Repo.save(existEmp);
		return "Record updated successfully";
	}

	public List<EmpEntity> getAllEmployees() {
		return Repo.findAll();
	}

	public EmpEntity getEmployeeById(Integer employeeCode) {
		return Repo.findById(employeeCode).orElseThrow(() -> new RuntimeException("Employee not found"));
	}

	public String deleteEmployee(Integer employeeid) {

		if (!Repo.existsById(employeeid)) {
			throw new RuntimeException("Employee not found");
		}

		Repo.deleteById(employeeid);
		return "Record deleted successfully";

	}

	public String deleteAllEmployees() {
		Repo.deleteAll();
		return "All employee records deleted successfully";
	}

}