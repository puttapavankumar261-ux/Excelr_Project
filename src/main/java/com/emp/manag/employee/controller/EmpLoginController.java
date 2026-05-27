package com.emp.manag.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.employee.entity.EmpLoginEntity;
import com.emp.manag.employee.service.EmpLoginService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class EmpLoginController {

	@Autowired
	private EmpLoginService empLoginService;

	@PostMapping("/savelogin")
	public EmpLoginEntity saveLogin(@RequestBody EmpLoginEntity login) {
		return empLoginService.saveLogin(login);
	}

	@PutMapping("/updatelogin/{loginId}")
	public String updateLogin(@PathVariable Integer loginId, @RequestBody EmpLoginEntity login) {
		return empLoginService.updateLogin(loginId, login);
	}

	@GetMapping("/getlogin/{loginId}")
	public EmpLoginEntity getLoginById(@PathVariable Integer loginId) {
		return empLoginService.getLoginById(loginId);
	}

	@GetMapping("/getloginbyusername/{username}")
	public EmpLoginEntity getLoginByUsername(@PathVariable String username) {
		return empLoginService.getLoginByUsername(username);
	}

	@GetMapping("/getalllogins")
	public List<EmpLoginEntity> getAllLogins() {
		return empLoginService.getAllLogins();
	}

	@DeleteMapping("/deletelogin/{loginId}")
	public String deleteLogin(@PathVariable Integer loginId) {
		return empLoginService.deleteLogin(loginId);
	}

	@DeleteMapping("/deletealllogins")
	public String deleteAllLogins() {
		return empLoginService.deleteAllLogins();
	}
}
