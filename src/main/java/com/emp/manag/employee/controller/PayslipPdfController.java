package com.emp.manag.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emp.manag.employee.service.PayslipPdfService;

@RestController
@RequestMapping("/api/employee-management")
public class PayslipPdfController {

    @Autowired
    private PayslipPdfService pdfService;

    @GetMapping(
            value = "/downloadpayslip/{payslipId}",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadPayslip(
            @PathVariable Integer payslipId) {

        byte[] pdf =
                pdfService.generatePdf(
                        payslipId);

        HttpHeaders headers =
                new HttpHeaders();

        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=payslip.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(
                        MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}