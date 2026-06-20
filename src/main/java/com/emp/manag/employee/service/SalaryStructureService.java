package com.emp.manag.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.SalaryStructureEntity;
import com.emp.manag.employee.entity.TaxSlabEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.employee.repo.SalaryStructureRepo;
import com.emp.manag.employee.repo.TaxSlabRepo;

@Service
public class SalaryStructureService {

    @Autowired
    private SalaryStructureRepo repo;

    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private TaxSlabRepo taxRepo;

    public SalaryStructureEntity save(
            SalaryStructureEntity salary) {

        Integer employeeId =
                salary.getEmployee().getEmployeeid();

        if (repo.existsByEmployeeEmployeeid(employeeId)) {
            throw new RuntimeException(
                    "Salary structure already exists");
        }

        EmpEntity employee =
                empRepo.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"));

        TaxSlabEntity tax =
                taxRepo.findById(
                        salary.getTaxSlab().getTaxid())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tax slab not found"));

        salary.setEmployee(employee);
        salary.setTaxSlab(tax);

        if (salary.getActive() == null) {
            salary.setActive(true);
        }

        return repo.save(salary);
    }

    public List<SalaryStructureEntity> getAll() {
        return repo.findAll();
    }

    public SalaryStructureEntity getByEmployee(
            Integer employeeId) {

        return repo.findByEmployeeEmployeeid(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Salary structure not found"));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}