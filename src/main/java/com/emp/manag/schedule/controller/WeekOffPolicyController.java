package com.emp.manag.schedule.controller;

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

import com.emp.manag.schedule.entity.WeekOffPolicyEntity;
import com.emp.manag.schedule.service.WeekOffPolicyService;

@RestController
@RequestMapping("/api/employee-management")
public class WeekOffPolicyController {

	@Autowired
	private WeekOffPolicyService weekOffPolicyService;

	@PostMapping("/saveweekoffpolicy")
	public WeekOffPolicyEntity saveWeekOffPolicy(@RequestBody WeekOffPolicyEntity policy) {
		return weekOffPolicyService.saveWeekOffPolicy(policy);
	}

	@PutMapping("/updateweekoffpolicy/{policyId}")
	public WeekOffPolicyEntity updateWeekOffPolicy(@PathVariable Integer policyId,
			@RequestBody WeekOffPolicyEntity policy) {
		return weekOffPolicyService.updateWeekOffPolicy(policyId, policy);
	}

	@GetMapping("/getweekoffpolicy/{policyId}")
	public WeekOffPolicyEntity getWeekOffPolicyById(@PathVariable Integer policyId) {
		return weekOffPolicyService.getWeekOffPolicyById(policyId);
	}

	@GetMapping("/getallweekoffpolicies")
	public List<WeekOffPolicyEntity> getAllWeekOffPolicies() {
		return weekOffPolicyService.getAllWeekOffPolicies();
	}

	@DeleteMapping("/deleteweekoffpolicy/{policyId}")
	public String deleteWeekOffPolicy(@PathVariable Integer policyId) {
		return weekOffPolicyService.deleteWeekOffPolicy(policyId);
	}
}
