package com.emp.manag.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.entity.PayrollEntity;

@Repository
public interface PayrollRepo extends JpaRepository<PayrollEntity, Integer> {

}
