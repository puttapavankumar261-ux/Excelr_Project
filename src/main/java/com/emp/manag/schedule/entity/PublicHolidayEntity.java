package com.emp.manag.schedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "public_holiday")
public class PublicHolidayEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "holiday_id")
	private Integer holidayId;

	@Column(name = "public_holiday_name", nullable = false)
	private String publicholidayName;

	@Column(name = "public_holiday_date", nullable = false, unique = true)
	private LocalDate publicholidayDate;

	@OneToMany(mappedBy = "publicHoliday")
	@JsonIgnore
	private List<AttendanceEntity> attendances;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
}
