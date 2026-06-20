package com.emp.manag.schedule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.emp.manag.schedule.dto.AttendanceDTO;
import com.emp.manag.schedule.dto.AttendanceSummaryDTO;
import com.emp.manag.schedule.entity.AttendanceEntity;
import com.emp.manag.schedule.repo.AttendanceRepo;
import com.emp.manag.schedule.service.AttendanceService;

@RestController
@RequestMapping("/api/employee-management")
public class AttendanceController {

    @Autowired
    private AttendanceService service;

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/saveattendance")
    public AttendanceEntity saveAttendance(
            @RequestBody AttendanceEntity attendance) {

        return service.saveAttendance(attendance);
    }

    @PostMapping("/attendance/checkin/{employeeId}")
    public AttendanceEntity checkIn(
            @PathVariable Integer employeeId) {

        return service.checkIn(employeeId);
    }
    @PutMapping("/attendance/checkout/{employeeId}")
    public AttendanceEntity checkOut(
            @PathVariable Integer employeeId) {

        return service.checkOut(employeeId);
    }
    @GetMapping("/attendance/today/{employeeId}")
    public AttendanceEntity getTodayAttendance(
            @PathVariable Integer employeeId) {

        return service.getTodayAttendance(
                employeeId);
    }
    @PutMapping("/updateattendance/{attendanceId}")
    public String updateAttendance(
            @PathVariable Integer attendanceId,
            @RequestBody AttendanceEntity attendance) {

        return service.updateAttendance(
                attendanceId,
                attendance);
    }

    @GetMapping("/getattendance/{attendanceId}")
    public AttendanceEntity getAttendanceById(
            @PathVariable Integer attendanceId) {

        return service.getAttendanceById(attendanceId);
    }

    @GetMapping("/getemployeeattendance/{employeeId}/{year}/{month}")
    public List<AttendanceEntity> getEmployeeMonthlyAttendance(
            @PathVariable Integer employeeId,
            @PathVariable Integer year,
            @PathVariable Integer month) {

        return service.getEmployeeMonthlyAttendance(
                employeeId,
                year,
                month);
    }

    @GetMapping("/monthlyworkingdays/{employeeId}/{year}/{month}")
    public Integer calculateMonthlyWorkingDays(
            @PathVariable Integer employeeId,
            @PathVariable Integer year,
            @PathVariable Integer month) {

        return service.calculateMonthlyWorkingDays(
                employeeId,
                year,
                month);
    }

    @DeleteMapping("/deleteattendancebyid/{attendanceId}")
    public String deleteAttendanceById(
            @PathVariable Integer attendanceId) {

        service.deleteById(attendanceId);

        return "Attendance record with ID "
                + attendanceId
                + " has been deleted.";
    }

    @GetMapping("/getallattendance")
    public List<AttendanceDTO> getAllAttendance() {

        return service.getAllAttendanceDTO();
    }

    // =============================
    // DEBUG APIs
    // =============================

    @GetMapping("/testcount")
    public String testCount() {

        return "JPA Count = "
                + attendanceRepo.count();
    }

    @GetMapping("/dbcheck")
    public String dbCheck() {

        return jdbcTemplate.queryForObject(
                "SELECT DATABASE()",
                String.class);
    }

    @GetMapping("/rawcount")
    public String rawCount() {

        Integer count =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM attendance",
                        Integer.class);

        return "Raw Count = " + count;
    }

    @GetMapping("/showtables")
    public Object showTables() {

        return jdbcTemplate.queryForList(
                "SHOW TABLES");
    }
    
    
    @GetMapping("/attendance-summary/{employeeId}")
    public AttendanceSummaryDTO getAttendanceSummary(
            @PathVariable Integer employeeId) {

        return service.getAttendanceSummary(employeeId);
    }
}