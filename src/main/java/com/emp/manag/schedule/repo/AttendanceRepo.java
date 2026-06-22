package com.emp.manag.schedule.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emp.manag.schedule.entity.AttendanceEntity;
import com.emp.manag.schedule.entity.AttendanceEntity.AttendanceStatus;

@Repository
public interface AttendanceRepo
        extends JpaRepository<AttendanceEntity, Integer> {

    boolean existsByEmployeeEmployeeidAndAttendanceDate(
            Integer employeeId,
            LocalDate attendanceDate);

    List<AttendanceEntity>
    findByEmployeeEmployeeidAndAttendanceDateBetween(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate);

    int countByEmployeeEmployeeidAndAttendanceDateBetweenAndAttendanceStatus(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate,
            AttendanceStatus attendanceStatus);

    @Query("""
            select coalesce(sum(a.totalWorkMinutes), 0)
            from AttendanceEntity a
            where a.employee.employeeid = :employeeId
              and a.attendanceDate between :startDate and :endDate
            """)
    Long sumTotalWorkMinutes(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate);

    @Query("""
            select coalesce(sum(a.overtimeMinutes), 0)
            from AttendanceEntity a
            where a.employee.employeeid = :employeeId
              and a.attendanceDate between :startDate and :endDate
            """)
    Long sumOvertimeMinutes(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate);

    int countByAttendanceDateAndAttendanceStatus(
            LocalDate attendanceDate,
            AttendanceStatus attendanceStatus);

    Optional<AttendanceEntity>
    findByEmployeeEmployeeidAndAttendanceDate(
            Integer employeeId,
            LocalDate attendanceDate);

    List<AttendanceEntity>
    findByEmployeeEmployeeidOrderByAttendanceDateDesc(
            Integer employeeId);
}