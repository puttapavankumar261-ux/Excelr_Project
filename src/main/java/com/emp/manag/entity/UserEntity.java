package com.emp.manag.entity;

import jakarta.persistence.Column;

public class UserEntity {
	

	@Column(name = "age")
	private Integer age;

	@Column(name = "email")
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber; // Use String to accommodate + and spaces/dashes

	@Column(name = "address")
	private String address;

}
