package com.emp.manag.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.entity.UserLoginEntity;

public interface UserLoginRepo extends JpaRepository<UserLoginEntity, Integer> {

	boolean existsByUsername(String username);

	boolean existsByUserUserLoginid(Integer userId);

	Optional<UserLoginEntity> findByUsername(String username);

}
