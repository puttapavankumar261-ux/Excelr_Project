package com.emp.manag.employee.controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
    public ResponseEntity<InputStreamResource>
    downloadPayslip(
            @PathVariable Integer payslipId) {

        ByteArrayInputStream pdf =
                new ByteArrayInputStream(
                        pdfService.generatePdf(
                                payslipId));

        HttpHeaders headers =
                new HttpHeaders();

        headers.add(
                "Content-Disposition",
                "inline; filename=payslip.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(
                        MediaType.APPLICATION_PDF)
                .body(
                        new InputStreamResource(
                                pdf));
    }
}
