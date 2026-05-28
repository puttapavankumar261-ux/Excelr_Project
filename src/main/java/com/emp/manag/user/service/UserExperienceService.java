package com.emp.manag.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.user.repo.UserExperienceRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserExperienceService {
	
	@Autowired
	private UserExperienceRepo userExpRepo;

}
