package com.emp.manag.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.user.entity.UserLoginEntity;
import com.emp.manag.user.service.UserLoginService;
import com.emp.manag.config.dto.LoginRequest;
import com.emp.manag.config.dto.SessionResponse;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/employee-management")
public class UserLoginController {
	
	@Autowired
	private UserLoginService loginService;

	@PostMapping("/savelogindetails")
	public UserLoginEntity saveLoginDetails(@RequestBody UserLoginEntity loginEntity) {
		return loginService.saveLogin(loginEntity);
	}
	
	@PutMapping("/updatelogindetails/{loginId}")
	public String updateLoginDetails(@PathVariable Integer loginId, @RequestBody UserLoginEntity updatedLogin) {
		return loginService.updateLogin(loginId, updatedLogin);
	}
	
	@DeleteMapping("/deletelogindetails/{loginId}")
	public String deleteLoginById(@PathVariable Integer loginId) {
		return loginService.deleteLogin(loginId);
	}
	
	@GetMapping("/getlogindetails/{loginId}")
	public UserLoginEntity getLoginDetails(@PathVariable Integer loginId) {
		return loginService.getLoginById(loginId);
	}
	
	@GetMapping("/getalllogindetails")
	public List<UserLoginEntity> getAllLoginDetails() {
		return loginService.getAllLogins();
	}
	
	@DeleteMapping("/deletealllogindetails")
	public String deleteAllLoginDetails() {
		return loginService.deleteAllLogins();
	}

	@PostMapping("/user/login")
	public SessionResponse userLogin(@RequestBody LoginRequest request, HttpSession session) {
		return loginService.login(request, session);
	}

	@GetMapping("/user/session")
	public SessionResponse userSession(HttpSession session) {
		return loginService.getSession(session);
	}

	@PostMapping("/user/logout")
	public SessionResponse userLogout(HttpSession session) {
		return loginService.logout(session);
	}
	
}
