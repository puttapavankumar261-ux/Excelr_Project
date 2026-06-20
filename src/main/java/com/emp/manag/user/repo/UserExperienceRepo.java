package com.emp.manag.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.user.entity.UserExperienceEntity;

@Repository
public interface UserExperienceRepo extends JpaRepository<UserExperienceEntity, Integer> {

}
