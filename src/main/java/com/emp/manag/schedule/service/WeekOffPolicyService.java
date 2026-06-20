package com.emp.manag.schedule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.schedule.entity.WeekOffPolicyEntity;
import com.emp.manag.schedule.repo.WeekOffPolicyRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class WeekOffPolicyService {

	@Autowired
	private WeekOffPolicyRepo weekOffPolicyRepo;

	public WeekOffPolicyEntity saveWeekOffPolicy(WeekOffPolicyEntity policy) {

		validateWeekOffPolicy(policy);

		if (weekOffPolicyRepo.existsByPolicyNameIgnoreCase(policy.getPolicyName())) {
			throw new RuntimeException("Week off policy already exists with name: " + policy.getPolicyName());
		}

		return weekOffPolicyRepo.save(policy);
	}

	public WeekOffPolicyEntity updateWeekOffPolicy(Integer policyId, WeekOffPolicyEntity updatedPolicy) {

		if (policyId == null) {
			throw new RuntimeException("Policy ID is required");
		}

		validateWeekOffPolicy(updatedPolicy);

		WeekOffPolicyEntity existingPolicy = getWeekOffPolicyById(policyId);
		existingPolicy.setPolicyName(updatedPolicy.getPolicyName());
		existingPolicy.setWeeklyOffDays(updatedPolicy.getWeeklyOffDays());

		return weekOffPolicyRepo.save(existingPolicy);
	}

	public WeekOffPolicyEntity getWeekOffPolicyById(Integer policyId) {

		if (policyId == null) {
			throw new RuntimeException("Policy ID is required");
		}

		return weekOffPolicyRepo.findById(policyId)
				.orElseThrow(() -> new RuntimeException("Week off policy not found with ID: " + policyId));
	}

	public List<WeekOffPolicyEntity> getAllWeekOffPolicies() {
		return weekOffPolicyRepo.findAll();
	}

	public String deleteWeekOffPolicy(Integer policyId) {

		WeekOffPolicyEntity existingPolicy = getWeekOffPolicyById(policyId);
		weekOffPolicyRepo.delete(existingPolicy);

		return "Week off policy deleted successfully";
	}

	private void validateWeekOffPolicy(WeekOffPolicyEntity policy) {

		if (policy == null) {
			throw new RuntimeException("Week off policy details are required");
		}

		if (policy.getPolicyName() == null || policy.getPolicyName().trim().isEmpty()) {
			throw new RuntimeException("Week off policy name is required");
		}

		if (policy.getWeeklyOffDays() == null || policy.getWeeklyOffDays() < 1 || policy.getWeeklyOffDays() > 2) {
			throw new RuntimeException("Weekly off days must be 1 or 2");
		}
	}
}
