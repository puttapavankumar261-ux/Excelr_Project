package com.emp.manag.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.entity.EmpEntity;

@Repository
public interface EmpRepo extends JpaRepository<EmpEntity, Integer> {

}
