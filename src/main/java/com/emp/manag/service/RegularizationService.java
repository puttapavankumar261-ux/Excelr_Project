package com.emp.manag.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emp.manag.entity.RegularizationEntity;
import com.emp.manag.repo.RegularizationRepo;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegularizationService {

	@Autowired
	private RegularizationRepo Repo2;

	public RegularizationEntity saveRegularization(RegularizationEntity regularization) {

		regularization.setRequestedstatus("Pending");
		regularization.setRequestedOn(LocalDateTime.now());

		System.out.println("Saving Regularization Data: " + regularization);
		return Repo2.save(regularization);
	}

	public RegularizationEntity updateRegularization(Integer sno, LocalTime punchintime, LocalTime punchouttime,
			RegularizationEntity updatedRegularization) {

		RegularizationEntity existReg = Repo2.findById(sno)
				.orElseThrow(() -> new RuntimeException("Regularization record not found"));

		existReg.setAttendancestatus(updatedRegularization.getAttendancestatus());
		existReg.setRequestedCheckIn(updatedRegularization.getRequestedCheckIn());
		existReg.setRequestedCheckOut(updatedRegularization.getRequestedCheckOut());
		existReg.setReason(updatedRegularization.getReason());
		existReg.setRemarks(updatedRegularization.getRemarks());
		existReg.setRequestedstatus(updatedRegularization.getRequestedstatus());

		return Repo2.save(existReg);
	}

	public String deleteById(Integer sno) {
		if (!Repo2.existsById(sno)) {
			throw new RuntimeException("Regularization record not found");
		}
		Repo2.deleteById(sno);
		return "Regularization record with ID " + sno + " deleted successfully.";
	}

}
