package com.emp.manag.employee.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.employee.entity.TaxSlabEntity;
import com.emp.manag.employee.service.TaxSlabService;

@RestController
@RequestMapping("/api/employee-management")
public class TaxSlabController {

	@Autowired
	private TaxSlabService taxSlabService;

	@PostMapping("/savetaxslab")
	public TaxSlabEntity saveTaxSlab(@RequestBody TaxSlabEntity taxSlab) {
		return taxSlabService.saveTaxSlab(taxSlab);
	}

	@PutMapping("/updatetaxslab/{taxid}")
	public TaxSlabEntity updateTaxSlab(
	        @PathVariable("taxid") Integer taxid,
	        @RequestBody TaxSlabEntity taxSlab) {

	    return taxSlabService.updateTaxSlab(taxid, taxSlab);
	}

	@GetMapping("/gettaxslab/{taxid}")
	public TaxSlabEntity getTaxSlabById(
	        @PathVariable("taxid") Integer taxid) {

	    return taxSlabService.getTaxSlabById(taxid);
	}

	@GetMapping("/getalltaxslabs")
	public List<TaxSlabEntity> getAllTaxSlabs() {
		return taxSlabService.getAllTaxSlabs();
	}

	@GetMapping("/calculateincometax/{taxableAmount}")
	public BigDecimal calculateIncomeTax(@PathVariable BigDecimal taxableAmount) {
		return taxSlabService.calculateIncomeTax(taxableAmount);
	}

	@DeleteMapping("/deletetaxslab/{taxid}")
	public String deleteTaxSlab(
	        @PathVariable("taxid") Integer taxid) {

	    return taxSlabService.deleteTaxSlab(taxid);
	}
}
