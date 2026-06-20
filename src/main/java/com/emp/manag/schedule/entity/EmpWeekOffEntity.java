package com.emp.manag.schedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.emp.manag.employee.entity.EmpEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "emp_week_off")
public class EmpWeekOffEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer weekOffId;

	@Column(name = "week_off_date", nullable = false)
	private LocalDate weekOffDate;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	@JsonIgnore
	private EmpEntity employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id")
	@JsonIgnore
	private WeekOffPolicyEntity weekOffPolicy;

	@OneToMany(mappedBy = "weekOff")
	@JsonIgnore
	private List<AttendanceEntity> attendances;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
