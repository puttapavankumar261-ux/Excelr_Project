package com.emp.manag.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.user.entity.UserEducationEntity;

@Repository
public interface UserEducationRepo extends JpaRepository<UserEducationEntity, Integer>{

}
