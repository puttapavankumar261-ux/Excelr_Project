package com.emp.manag.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.emp.manag.employee.entity.SalaryStructureEntity;
import com.emp.manag.employee.service.SalaryStructureService;

@RestController
@RequestMapping("/api/employee-management")
public class SalaryStructureController {

    @Autowired
    private SalaryStructureService service;

    @PostMapping("/savesalarystructure")
    public SalaryStructureEntity save(
            @RequestBody SalaryStructureEntity salary) {

        return service.save(salary);
    }

    @GetMapping("/getsalarystructure/{employeeId}")
    public SalaryStructureEntity getByEmployee(
            @PathVariable Integer employeeId) {

        return service.getByEmployee(employeeId);
    }

    @GetMapping("/getallsalarystructures")
    public List<SalaryStructureEntity> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/deletesalarystructure/{id}")
    public String delete(@PathVariable Integer id) {

        service.delete(id);

        return "Salary Structure Deleted Successfully";
    }
}