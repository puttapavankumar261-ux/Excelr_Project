package com.emp.manag.schedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.employee.entity.EmpEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "attendance" ,
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "attendance_date"})
    })
public class AttendanceEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendance_id", unique= true, nullable=false)
	private Integer attendanceId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private EmpEntity employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shift_id")
	private ShiftEntity shift;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "holiday_id")
	private PublicHolidayEntity publicHoliday;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "week_off_id")
	private EmpWeekOffEntity weekOff;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leave_id")
	private LeaveEntity leave;

	@Column(name = "attendance_date", nullable = false, updatable = false)
	private LocalDate attendanceDate;

	@Column(name = "punch_in_time")
	private LocalTime punchInTime;

	@Column(name = "punch_out_time")
	private LocalTime punchOutTime;
	
	@Column(name = "late_by_minutes")
	private Long lateByMinutes;
	
	@Column(name = "early_exit_minutes")
	private Long earlyExitMinutes;
	
	@Column(name = "total_work_minutes")
	private Long totalWorkMinutes;
	
	@Column(name = "overtime_minutes")
	private Long overtimeMinutes;

	@Column(name = "attendance_status", nullable = false)  // PRESENT, ABSENT, LEAVE, HOLIDAY
	@Enumerated(EnumType.STRING)
	private AttendanceStatus attendanceStatus;
	
	public enum AttendanceStatus {

	    PRESENT,
	    ABSENT,
	    HALF_DAY,
	    LEAVE,
	    HOLIDAY,
	    WEEK_OFF
	}
	
	@OneToOne(mappedBy = "attendance")
	private RegularizationEntity regularization;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false, insertable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	   

}
