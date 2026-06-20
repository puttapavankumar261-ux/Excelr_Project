package com.emp.manag.employee.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.employee.entity.TaxSlabEntity;
import com.emp.manag.employee.repo.TaxSlabRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaxSlabService {

	@Autowired
	private TaxSlabRepo taxSlabRepo;

	public TaxSlabEntity saveTaxSlab(TaxSlabEntity taxSlab) {

		validateTaxSlab(taxSlab);

		if (taxSlabRepo.existsBySlabNameIgnoreCase(taxSlab.getSlabName())) {
			throw new RuntimeException("Tax slab already exists with name: " + taxSlab.getSlabName());
		}

		if (taxSlab.getActive() == null) {
			taxSlab.setActive(true);
		}

		return taxSlabRepo.save(taxSlab);
	}

	public TaxSlabEntity updateTaxSlab(Integer taxid, TaxSlabEntity updatedTaxSlab) {

		if (taxid == null) {
			throw new RuntimeException("Tax slab ID is required");
		}

		validateTaxSlab(updatedTaxSlab);

		TaxSlabEntity existingTaxSlab = getTaxSlabById(taxid);
		existingTaxSlab.setSlabName(updatedTaxSlab.getSlabName());
		existingTaxSlab.setMinAmount(updatedTaxSlab.getMinAmount());
		existingTaxSlab.setMaxAmount(updatedTaxSlab.getMaxAmount());
		existingTaxSlab.setPercentage(updatedTaxSlab.getPercentage());
		existingTaxSlab.setActive(updatedTaxSlab.getActive());
		existingTaxSlab.setTaxregimeType(updatedTaxSlab.getTaxregimeType());

		return taxSlabRepo.save(existingTaxSlab);
	}

	public TaxSlabEntity getTaxSlabById(Integer taxid) {

		if (taxid == null) {
			throw new RuntimeException("Tax slab ID is required");
		}

		return taxSlabRepo.findById(taxid)
				.orElseThrow(() -> new RuntimeException("Tax slab not found with ID: " + taxid));
	}

	public List<TaxSlabEntity> getAllTaxSlabs() {
		return taxSlabRepo.findAll();
	}

	public BigDecimal calculateIncomeTax(BigDecimal taxableAmount) {

		if (taxableAmount == null || taxableAmount.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		}

		return taxSlabRepo.findActiveSlabForAmount(taxableAmount)
				.map(slab -> taxableAmount.multiply(slab.getPercentage()).divide(BigDecimal.valueOf(100)))
				.orElse(BigDecimal.ZERO);
	}

	public String deleteTaxSlab(Integer taxid) {

		TaxSlabEntity taxSlab = getTaxSlabById(taxid);
		taxSlabRepo.delete(taxSlab);

		return "Tax slab deleted successfully";
	}

	private void validateTaxSlab(TaxSlabEntity taxSlab) {

		if (taxSlab == null) {
			throw new RuntimeException("Tax slab details are required");
		}

		if (taxSlab.getSlabName() == null || taxSlab.getSlabName().trim().isEmpty()) {
			throw new RuntimeException("Tax slab name is required");
		}

		if (taxSlab.getMinAmount() == null || taxSlab.getMaxAmount() == null) {
			throw new RuntimeException("Minimum and maximum amount are required");
		}

		if (taxSlab.getMinAmount().compareTo(BigDecimal.ZERO) < 0 || taxSlab.getMaxAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("Tax slab amounts cannot be negative");
		}

		if (taxSlab.getMaxAmount().compareTo(taxSlab.getMinAmount()) < 0) {
			throw new RuntimeException("Maximum amount cannot be less than minimum amount");
		}

		if (taxSlab.getPercentage() == null || taxSlab.getPercentage().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("Tax percentage must be zero or greater");
		}
	}
}
