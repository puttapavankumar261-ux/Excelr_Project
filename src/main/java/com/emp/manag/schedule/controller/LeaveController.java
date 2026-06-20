package com.emp.manag.schedule.controller;

import java.time.LocalDate;
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

import com.emp.manag.schedule.dto.LeaveDTO;
import com.emp.manag.schedule.entity.LeaveEntity;
import com.emp.manag.schedule.entity.LeaveEntity.ApprovalStatus;
import com.emp.manag.schedule.service.LeaveService;

@RestController
@RequestMapping("/api/employee-management")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;

	@PostMapping("/saveleave")
	public LeaveEntity saveLeave(@RequestBody LeaveEntity leave) {
		return leaveService.saveLeave(leave);
	}

	@PutMapping("/updateleave/{leaveId}")
	public LeaveEntity updateLeave(@PathVariable Integer leaveId, @RequestBody LeaveEntity leave) {
		return leaveService.updateLeave(leaveId, leave);
	}

	@PutMapping("/teamleadreviewleave/{leaveId}/{approverId}")
	public LeaveEntity approveByTeamLead(@PathVariable Integer leaveId, @PathVariable Integer approverId) {
		return leaveService.approveByTeamLead(leaveId, approverId);
	}

	@PutMapping("/sendleavetomanager/{leaveId}")
	public LeaveEntity sendToManager(@PathVariable Integer leaveId) {
		return leaveService.sendToManager(leaveId);
	}

	@PutMapping("/managerreviewleave/{leaveId}/{approverId}")
	public LeaveEntity approveByManager(@PathVariable Integer leaveId, @PathVariable Integer approverId) {
		return leaveService.approveByManager(leaveId, approverId);
	}

	@PutMapping("/sendleavetohr/{leaveId}")
	public LeaveEntity sendToHr(@PathVariable Integer leaveId) {
		return leaveService.sendToHr(leaveId);
	}

	@PutMapping("/hrreviewleave/{leaveId}/{approverId}")
	public LeaveEntity approveByHr(@PathVariable Integer leaveId, @PathVariable Integer approverId) {
		return leaveService.approveByHr(leaveId, approverId);
	}

	@PutMapping("/approveleave/{leaveId}")
	public LeaveEntity approveLeave(@PathVariable Integer leaveId) {
		return leaveService.approveLeave(leaveId);
	}

	@PutMapping("/rejectleave/{leaveId}/{rejectedById}")
	public LeaveEntity rejectLeave(@PathVariable Integer leaveId, @PathVariable Integer rejectedById,
			@RequestBody String rejectionReason) {
		return leaveService.rejectLeave(leaveId, rejectedById, rejectionReason);
	}

	@PutMapping("/cancelleave/{leaveId}")
	public LeaveEntity cancelLeave(
	        @PathVariable Integer leaveId) {

	    return leaveService.cancelLeave(
	            leaveId);
	}
	
	@GetMapping("/getleave/{leaveId}")
	public LeaveEntity getLeaveById(@PathVariable Integer leaveId) {
		return leaveService.getLeaveById(leaveId);
	}

	@GetMapping("/getallleaves")
	public List<LeaveDTO> getAllLeaves() {
	    return leaveService.getAllLeaveDTO();
	}

	@GetMapping("/getemployeeleaves/{employeeId}")
	public List<LeaveEntity> getLeavesByEmployee(@PathVariable Integer employeeId) {
		return leaveService.getLeavesByEmployee(employeeId);
	}

	@GetMapping("/getleavesbystatus/{approvalStatus}")
	public List<LeaveEntity> getLeavesByApprovalStatus(@PathVariable ApprovalStatus approvalStatus) {
		return leaveService.getLeavesByApprovalStatus(approvalStatus);
	}

	@GetMapping("/calculateleavedays/{employeeId}/{startDate}/{endDate}")
	public Integer calculateLeaveDays(@PathVariable Integer employeeId, @PathVariable LocalDate startDate,
			@PathVariable LocalDate endDate) {
		return leaveService.calculateLeaveDays(employeeId, startDate, endDate);
	}

	@DeleteMapping("/deleteleave/{leaveId}")
	public String deleteLeave(@PathVariable Integer leaveId) {
		return leaveService.deleteLeave(leaveId);
	}
}
