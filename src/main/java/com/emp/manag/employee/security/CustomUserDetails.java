package com.emp.manag.employee.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.emp.manag.employee.entity.EmpLoginEntity;
import com.emp.manag.employee.repo.EmpLoginRepo;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private EmpLoginRepo loginRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        EmpLoginEntity user =
                loginRepo.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles("EMPLOYEE")
                .build();
    }
}