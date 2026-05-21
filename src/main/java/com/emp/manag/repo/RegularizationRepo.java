package com.emp.manag.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.entity.RegularizationEntity;

@Repository
public interface RegularizationRepo extends JpaRepository<RegularizationEntity, Integer> {

}
