package com.emp.manag.schedule.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.repo.EmpRepo;
import com.emp.manag.schedule.entity.LeaveEntity;
import com.emp.manag.schedule.entity.LeaveEntity.ApprovalStatus;
import com.emp.manag.schedule.entity.LeaveEntity.LeaveType;
import com.emp.manag.schedule.repo.EmpWeekOffRepo;
import com.emp.manag.schedule.repo.LeaveRepo;
import com.emp.manag.schedule.repo.PublicHolidayRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class LeaveService {

	@Autowired
	private LeaveRepo leaveRepo;

	@Autowired
	private EmpRepo empRepo;

	@Autowired
	private EmpWeekOffRepo empWeekOffRepo;

	@Autowired
	private PublicHolidayRepo publicHolidayRepo;

	public LeaveEntity saveLeave(LeaveEntity leave) {

		validateLeaveRequest(leave);

		Integer employeeId = leave.getEmployee().getEmployeeid();

		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		validateNoOverlappingLeave(employeeId, leave.getLeaveStartDate(), leave.getLeaveEndDate());
		validateMinimumWorkingDaysInAffectedWeeks(employeeId, leave);

		leave.setEmployee(employee);
		leave.setApprovalStatus(ApprovalStatus.PENDING_TEAM_LEAD);
		leave.setLeaveDays(calculateLeaveDays(employeeId, leave.getLeaveStartDate(), leave.getLeaveEndDate()));
		leave.setEmployeeApprover(null);
		leave.setApprovedOn(null);
		leave.setRejectedBy(null);
		leave.setRejectedOn(null);
		leave.setRejectionReason(null);

		return leaveRepo.save(leave);
	}

	public LeaveEntity updateLeave(Integer leaveId, LeaveEntity updatedLeave) {

		if (leaveId == null) {
			throw new RuntimeException("Leave ID is required");
		}

		validateLeaveRequest(updatedLeave);

		LeaveEntity existingLeave = leaveRepo.findById(leaveId)
				.orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + leaveId));

		if (existingLeave.getApprovalStatus() == ApprovalStatus.APPROVED
				|| existingLeave.getApprovalStatus() == ApprovalStatus.REJECTED) {
			throw new RuntimeException("Approved or rejected leave cannot be updated");
		}

		Integer employeeId = updatedLeave.getEmployee().getEmployeeid();

		EmpEntity employee = empRepo.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		validateNoOverlappingLeaveExcludingId(leaveId, employeeId, updatedLeave.getLeaveStartDate(),
				updatedLeave.getLeaveEndDate());
		validateMinimumWorkingDaysInAffectedWeeks(employeeId, updatedLeave);

		existingLeave.setEmployee(employee);
		existingLeave.setLeaveStartDate(updatedLeave.getLeaveStartDate());
		existingLeave.setLeaveEndDate(updatedLeave.getLeaveEndDate());
		existingLeave.setLeaveType(updatedLeave.getLeaveType());
		existingLeave.setLeaveDays(calculateLeaveDays(employeeId, updatedLeave.getLeaveStartDate(),
				updatedLeave.getLeaveEndDate()));

		return leaveRepo.save(existingLeave);
	}

	public LeaveEntity approveByTeamLead(Integer leaveId, Integer approverId) {
		return moveToNextApprovalStatus(leaveId, approverId, ApprovalStatus.PENDING_TEAM_LEAD,
				ApprovalStatus.TEAM_LEAD_REVIEWED);
	}

	public LeaveEntity sendToManager(Integer leaveId) {
		return moveStatusWithoutApprover(leaveId, ApprovalStatus.TEAM_LEAD_REVIEWED,
				ApprovalStatus.PENDING_MANAGER);
	}

	public LeaveEntity approveByManager(Integer leaveId, Integer approverId) {
		return moveToNextApprovalStatus(leaveId, approverId, ApprovalStatus.PENDING_MANAGER,
				ApprovalStatus.MANAGER_REVIEWED);
	}

	public LeaveEntity sendToHr(Integer leaveId) {
		return moveStatusWithoutApprover(leaveId, ApprovalStatus.MANAGER_REVIEWED,
				ApprovalStatus.PENDING_HR);
	}

	public LeaveEntity approveByHr(Integer leaveId, Integer approverId) {

		LeaveEntity leave = moveToNextApprovalStatus(leaveId, approverId, ApprovalStatus.PENDING_HR,
				ApprovalStatus.HR_REVIEWED);

		return leaveRepo.save(leave);
	}

	public LeaveEntity approveLeave(Integer leaveId) {

		if (leaveId == null) {
			throw new RuntimeException("Leave ID is required");
		}

		LeaveEntity leave = leaveRepo.findById(leaveId)
				.orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + leaveId));

		if (leave.getApprovalStatus() != ApprovalStatus.HR_REVIEWED) {
			throw new RuntimeException("Only HR reviewed leave requests can be approved");
		}

		leave.setApprovalStatus(ApprovalStatus.APPROVED);
		leave.setApprovedOn(LocalDateTime.now());
		leave.setRejectedBy(null);
		leave.setRejectedOn(null);
		leave.setRejectionReason(null);

		return leaveRepo.save(leave);
	}

	public LeaveEntity rejectLeave(Integer leaveId, Integer rejectedById, String rejectionReason) {

		if (leaveId == null) {
			throw new RuntimeException("Leave ID is required");
		}

		if (rejectedById == null) {
			throw new RuntimeException("Rejected by employee ID is required");
		}

		if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
			throw new RuntimeException("Rejection reason is required");
		}

		LeaveEntity leave = leaveRepo.findById(leaveId)
				.orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + leaveId));

		if (leave.getApprovalStatus() == ApprovalStatus.APPROVED
				|| leave.getApprovalStatus() == ApprovalStatus.REJECTED) {
			throw new RuntimeException("Only pending leave requests can be rejected");
		}

		EmpEntity rejectedBy = empRepo.findById(rejectedById)
				.orElseThrow(() -> new RuntimeException("Rejector not found with ID: " + rejectedById));

		leave.setApprovalStatus(ApprovalStatus.REJECTED);
		leave.setRejectedBy(rejectedBy);
		leave.setRejectedOn(LocalDateTime.now());
		leave.setRejectionReason(rejectionReason);

		return leaveRepo.save(leave);
	}

	public LeaveEntity getLeaveById(Integer leaveId) {

		if (leaveId == null) {
			throw new RuntimeException("Leave ID is required");
		}

		return leaveRepo.findById(leaveId)
				.orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + leaveId));
	}

	public List<LeaveEntity> getAllLeaves() {
		return leaveRepo.findAll();
	}

	public List<LeaveEntity> getLeavesByEmployee(Integer employeeId) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		return leaveRepo.findByEmployeeEmployeeid(employeeId);
	}

	public List<LeaveEntity> getLeavesByApprovalStatus(ApprovalStatus approvalStatus) {

		if (approvalStatus == null) {
			throw new RuntimeException("Approval status is required");
		}

		return leaveRepo.findByApprovalStatus(approvalStatus);
	}

	public String deleteLeave(Integer leaveId) {

		if (leaveId == null) {
			throw new RuntimeException("Leave ID is required");
		}

		LeaveEntity leave = leaveRepo.findById(leaveId)
				.orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + leaveId));

		if (leave.getApprovalStatus() == ApprovalStatus.APPROVED) {
			throw new RuntimeException("Approved leave cannot be deleted");
		}

		leaveRepo.delete(leave);

		return "Leave record deleted successfully";
	}

	public Integer calculateLeaveDays(Integer employeeId, LocalDate startDate, LocalDate endDate) {

		validateEmployeeAndDateRange(employeeId, startDate, endDate);

		int leaveDays = 0;

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			if (isChargeableLeaveDate(employeeId, date)) {
				leaveDays++;
			}
		}

		return leaveDays;
	}

	private LeaveEntity moveToNextApprovalStatus(Integer leaveId, Integer approverId,
			ApprovalStatus requiredCurrentStatus, ApprovalStatus nextStatus) {

		if (leaveId == null) {
			throw new RuntimeException("Leave ID is required");
		}

		if (approverId == null) {
			throw new RuntimeException("Approver employee ID is required");
		}

		LeaveEntity leave = leaveRepo.findById(leaveId)
				.orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + leaveId));

		if (leave.getApprovalStatus() != requiredCurrentStatus) {
			throw new RuntimeException("Leave request is not in " + requiredCurrentStatus + " status");
		}

		EmpEntity approver = empRepo.findById(approverId)
				.orElseThrow(() -> new RuntimeException("Approver not found with ID: " + approverId));

		leave.setApprovalStatus(nextStatus);
		leave.setEmployeeApprover(approver);

		return leaveRepo.save(leave);
	}

	private LeaveEntity moveStatusWithoutApprover(Integer leaveId, ApprovalStatus requiredCurrentStatus,
			ApprovalStatus nextStatus) {

		if (leaveId == null) {
			throw new RuntimeException("Leave ID is required");
		}

		LeaveEntity leave = leaveRepo.findById(leaveId)
				.orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + leaveId));

		if (leave.getApprovalStatus() != requiredCurrentStatus) {
			throw new RuntimeException("Leave request is not in " + requiredCurrentStatus + " status");
		}

		leave.setApprovalStatus(nextStatus);

		return leaveRepo.save(leave);
	}

	private void validateLeaveRequest(LeaveEntity leave) {

		if (leave == null) {
			throw new RuntimeException("Leave details are required");
		}

		if (leave.getEmployee() == null || leave.getEmployee().getEmployeeid() == null) {
			throw new RuntimeException("Employee ID is required");
		}

		if (leave.getLeaveStartDate() == null) {
			throw new RuntimeException("Leave start date is required");
		}

		if (leave.getLeaveEndDate() == null) {
			throw new RuntimeException("Leave end date is required");
		}

		if (leave.getLeaveEndDate().isBefore(leave.getLeaveStartDate())) {
			throw new RuntimeException("Leave end date cannot be before start date");
		}

		if (leave.getLeaveType() == null) {
			throw new RuntimeException("Leave type is required");
		}
	}

	private void validateEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate) {

		if (employeeId == null) {
			throw new RuntimeException("Employee ID is required");
		}

		if (startDate == null || endDate == null) {
			throw new RuntimeException("Start date and end date are required");
		}

		if (endDate.isBefore(startDate)) {
			throw new RuntimeException("End date cannot be before start date");
		}
	}

	private void validateNoOverlappingLeave(Integer employeeId, LocalDate startDate, LocalDate endDate) {

		if (leaveRepo.existsActiveLeaveOverlap(employeeId, startDate, endDate, ApprovalStatus.REJECTED)) {
			throw new RuntimeException("Leave request already exists for this employee in the selected date range");
		}
	}

	private void validateNoOverlappingLeaveExcludingId(Integer leaveId, Integer employeeId,
			LocalDate startDate, LocalDate endDate) {

		if (leaveRepo.existsActiveLeaveOverlapExcludingId(leaveId, employeeId, startDate, endDate,
				ApprovalStatus.REJECTED)) {
			throw new RuntimeException("Leave request already exists for this employee in the selected date range");
		}
	}

	private void validateMinimumWorkingDaysInAffectedWeeks(Integer employeeId, LeaveEntity leave) {

		if (leave.getLeaveType() == LeaveType.SICK) {
			return;
		}

		LocalDate weekStart = leave.getLeaveStartDate()
				.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastWeekStart = leave.getLeaveEndDate()
				.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

		for (LocalDate currentWeekStart = weekStart; !currentWeekStart.isAfter(lastWeekStart);
				currentWeekStart = currentWeekStart.plusWeeks(1)) {

			LocalDate currentWeekEnd = currentWeekStart.plusDays(6);

			int companyWorkingDays = countCompanyWorkingDays(employeeId, currentWeekStart, currentWeekEnd);
			int requestedLeaveDays = countRequestedLeaveDaysInRange(employeeId, leave.getLeaveStartDate(),
					leave.getLeaveEndDate(), currentWeekStart, currentWeekEnd);

			if (companyWorkingDays >= 2 && companyWorkingDays - requestedLeaveDays < 2) {
				throw new RuntimeException("At least 2 working days are required in a week after leave, week offs and holidays");
			}
		}
	}

	private int countCompanyWorkingDays(Integer employeeId, LocalDate startDate, LocalDate endDate) {

		int workingDays = 0;

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			boolean isWeekOff = empWeekOffRepo.existsByEmployeeEmployeeidAndWeekOffDate(employeeId, date);
			boolean isPublicHoliday = publicHolidayRepo.existsByPublicholidayDate(date);

			if (!isWeekOff && !isPublicHoliday) {
				workingDays++;
			}
		}

		return workingDays;
	}

	private int countRequestedLeaveDaysInRange(Integer employeeId, LocalDate leaveStartDate,
			LocalDate leaveEndDate, LocalDate rangeStartDate, LocalDate rangeEndDate) {

		int leaveDays = 0;
		LocalDate startDate = leaveStartDate.isAfter(rangeStartDate) ? leaveStartDate : rangeStartDate;
		LocalDate endDate = leaveEndDate.isBefore(rangeEndDate) ? leaveEndDate : rangeEndDate;

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			if (isChargeableLeaveDate(employeeId, date)) {
				leaveDays++;
			}
		}

		return leaveDays;
	}

	private boolean isChargeableLeaveDate(Integer employeeId, LocalDate date) {

		boolean isWeekOff = empWeekOffRepo.existsByEmployeeEmployeeidAndWeekOffDate(employeeId, date);
		boolean isPublicHoliday = publicHolidayRepo.existsByPublicholidayDate(date);

		return !isWeekOff && !isPublicHoliday;
	}
}
