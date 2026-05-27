package com.emp.manag.employee.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emp.manag.employee.entity.TaxSlabEntity;

@Repository
public interface TaxSlabRepo extends JpaRepository<TaxSlabEntity, Integer> {

	List<TaxSlabEntity> findByActiveTrue();

	boolean existsBySlabNameIgnoreCase(String slabName);

	@Query("""
			select t
			from TaxSlabEntity t
			where t.active = true
			  and :amount >= t.minAmount
			  and :amount <= t.maxAmount
			order by t.minAmount asc
			""")
	Optional<TaxSlabEntity> findActiveSlabForAmount(BigDecimal amount);
}
