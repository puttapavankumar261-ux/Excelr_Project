package com.emp.manag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.entity.AttendanceEntity;
import com.emp.manag.entity.EmpEntity;
import com.emp.manag.repo.AttendanceRepo;
import com.emp.manag.repo.EmpRepo;


@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceService {
	
	@Autowired
	private AttendanceRepo Repo2;

	@Autowired
	private EmpRepo empRepo1;

	public AttendanceEntity saveAttendance(AttendanceEntity attendance, Integer employeeSno) {
	
		EmpEntity existEmp = empRepo1.findById(employeeSno)
				.orElseThrow(() -> new RuntimeException("Employee not found with sno: " + employeeSno));
		
		attendance.setEmployee(existEmp);
		return Repo2.save(attendance);
	}

	public List<AttendanceEntity> getAllAttendances() {
		return Repo2.findAll();
	}

	public AttendanceEntity deleteById(Integer attendanceId) {
		AttendanceEntity existAtt = Repo2.findById(attendanceId)
				.orElseThrow(() -> new RuntimeException("Attendance record not found"));
		Repo2.delete(existAtt);
		return existAtt;
	}

	public String deleteAllAttendances() {
		Repo2.deleteAll();
		return "All attendance records have been deleted.";
	}
}