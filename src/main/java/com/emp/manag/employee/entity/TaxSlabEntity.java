package com.emp.manag.employee.entity;

import java.math.BigDecimal;
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
@Table(name = "tax_slab")
public class TaxSlabEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="tax_id")
	private Integer taxid;

	@Column(name = "slab_name", nullable = false)
	private String slabName;

	@Column(name = "min_amount", nullable = false)
	private BigDecimal minAmount;

	@Column(name = "max_amount", nullable = false)
	private BigDecimal maxAmount;

	@Column(name = "percentage", nullable = false)
	private BigDecimal percentage;
	
	@Column(name = "tax_regime_Type")
	private String taxregimeType;//old or new

	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@CreationTimestamp
	@Column(name= "created_at")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name= "updated_at")
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy = "taxSlab")
	@JsonIgnore
	private List<PayrollEntity> payrolls;
	
}
