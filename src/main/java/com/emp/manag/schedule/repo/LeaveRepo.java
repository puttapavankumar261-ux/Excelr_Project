package com.emp.manag.schedule.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.LeaveEntity;
import com.emp.manag.schedule.entity.LeaveEntity.ApprovalStatus;

@Repository
public interface LeaveRepo extends JpaRepository<LeaveEntity, Integer> {

	List<LeaveEntity> findByEmployeeEmployeeid(Integer employeeId);

	List<LeaveEntity> findByApprovalStatus(ApprovalStatus approvalStatus);

	int countByEmployeeEmployeeidAndApprovalStatusAndLeaveTypeAndLeaveStartDateLessThanEqualAndLeaveEndDateGreaterThanEqual(
			Integer employeeId, ApprovalStatus approvalStatus, LeaveEntity.LeaveType leaveType,
			LocalDate endDate, LocalDate startDate);

	@Query("""
			select coalesce(sum(l.leaveDays), 0)
			from LeaveEntity l
			where l.employee.employeeid = :employeeId
			  and l.approvalStatus = :approvalStatus
			  and l.leaveType = :leaveType
			  and l.leaveStartDate <= :endDate
			  and l.leaveEndDate >= :startDate
			""")
	Integer sumLeaveDaysByType(Integer employeeId, ApprovalStatus approvalStatus, LeaveEntity.LeaveType leaveType,
			LocalDate startDate, LocalDate endDate);

	@Query("""
			select count(l) > 0
			from LeaveEntity l
			where l.employee.employeeid = :employeeId
			  and l.approvalStatus <> :excludedStatus
			  and l.leaveStartDate <= :endDate
			  and l.leaveEndDate >= :startDate
			""")
	boolean existsActiveLeaveOverlap(Integer employeeId, LocalDate startDate, LocalDate endDate,
			ApprovalStatus excludedStatus);

	@Query("""
			select count(l) > 0
			from LeaveEntity l
			where l.leaveId <> :leaveId
			  and l.employee.employeeid = :employeeId
			  and l.approvalStatus <> :excludedStatus
			  and l.leaveStartDate <= :endDate
			  and l.leaveEndDate >= :startDate
			""")
	boolean existsActiveLeaveOverlapExcludingId(Integer leaveId, Integer employeeId, LocalDate startDate,
			LocalDate endDate, ApprovalStatus excludedStatus);
	
	@Query("""
		       SELECT l
		       FROM LeaveEntity l
		       WHERE l.employee.employeeid = :employeeId
		       AND l.approvalStatus = 'APPROVED'
		       AND :today BETWEEN l.leaveStartDate
		                      AND l.leaveEndDate
		       """)
		List<LeaveEntity> findActiveApprovedLeave(
		        Integer employeeId,
		        LocalDate today);
}
