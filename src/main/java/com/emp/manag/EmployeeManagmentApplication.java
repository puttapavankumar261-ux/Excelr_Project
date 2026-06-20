package com.emp.manag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
// import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = { "com.emp.manag.employee", "com.emp.manag.jobboard", "com.emp.manag.user", "com.emp.manag.schedule", "com.emp.manag.config", "com.emp.manag.setup" })
@EntityScan(basePackages = { "com.emp.manag.employee", "com.emp.manag.jobboard", "com.emp.manag.user", "com.emp.manag.schedule", "com.emp.manag.config" })
public class EmployeeManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagmentApplication.class, args);
	}

}
