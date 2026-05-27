package com.emp.manag.schedule.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shift")
public class ShiftEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shift_id")
	private Integer shiftid;

	@OneToMany(mappedBy = "shift")
	private List<EmpEntity> employees;
	
	@OneToMany(mappedBy = "shift")
	private List<AttendanceEntity> attendances;

	@Enumerated(EnumType.STRING)
	@Column(name = "shift_type", nullable = false)
	private ShiftType shiftType;

	public enum ShiftType {
	    MORNING,
	    NIGHT,
	    US,
	    ROTATIONAL
	}

	@Column(name = "shift_name", nullable = false)
	private String shiftName;

	// Shift Hierarchy (Parent)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_shift_id")
	private ShiftEntity parentShift;

	@OneToMany(mappedBy = "parentShift")
	private List<ShiftEntity> childShifts;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	@Column(name = "cross_day", nullable = false)
	private Boolean crossDay; // true for night shifts

	// ---- Grace & Rules ----

	@Column(name = "late_grace_minutes", nullable = false)
	private Integer lateGraceMinutes;

	@Column(name = "early_exit_grace_minutes")
	private Integer earlyExitGraceMinutes;

	@Column(name = "min_work_hours")
	private BigDecimal minWorkHours;

	// ---- Metadata ----

	@Column(name = "active", nullable = false)
	private Boolean active;

	@CreationTimestamp
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false)
	private LocalDateTime updatedAt;
}
