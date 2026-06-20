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

import com.emp.manag.user.entity.UserEntity;
import com.emp.manag.user.service.UserService;

@RestController
@RequestMapping("/api/employee-management")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/saveuser")
	public UserEntity saveUser(@RequestBody UserEntity user) {
		return userService.saveUser(user);
	}

	@PutMapping("/updateuser/{userId}")
	public String updateUser(@PathVariable Integer userId, @RequestBody UserEntity updatedUser) {
		return userService.updateUser(userId, updatedUser);
	}

	@GetMapping("/getuser/{userId}")
	public UserEntity getUserById(@PathVariable Integer userId) {
		return userService.getUserById(userId);
	}

	@GetMapping("/getallusers")
	public List<UserEntity> getAllUsers() {
		return userService.getAllUsers();
	}

	@DeleteMapping("/deleteuserbyid/{userId}")
	public String deleteUserById(Integer userId) {
		return userService.deleteUserById(userId);
	}

	@DeleteMapping("/deleteallusers")
	public List<UserEntity> deleteAllUsers() {
		return userService.getAllUsers();
	}

}
