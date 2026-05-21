package com.emp.manag.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emp.manag.entity.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

}
