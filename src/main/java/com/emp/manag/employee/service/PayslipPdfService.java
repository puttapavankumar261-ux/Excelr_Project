package com.emp.manag.employee.service;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.manag.employee.entity.PayslipEntity;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PayslipPdfService {

    @Autowired
    private PayslipService payslipService;

    public byte[] generatePdf(Integer payslipId) {

        try {

            PayslipEntity payslip =
                    payslipService.getPayslipById(
                            payslipId);

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            Document document =
                    new Document();

            PdfWriter.getInstance(
                    document,
                    out);

            document.open();

            document.add(
                    new Paragraph(
                            "EMPLOYEE PAYSLIP"));

            document.add(
                    new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Employee : "
                                    + payslip.getEmployee()
                                    .getEmployeename()));

            document.add(
                    new Paragraph(
                            "Payslip No : "
                                    + payslip.getPayslipNumber()));

            document.add(
                    new Paragraph(
                            "Month : "
                                    + payslip.getMonth()
                                    + "/"
                                    + payslip.getYear()));

            document.add(
                    new Paragraph(
                            "Gross Earnings : ₹"
                                    + payslip.getGrossEarnings()));

            document.add(
                    new Paragraph(
                            "Deductions : ₹"
                                    + payslip.getTotalDeductions()));

            document.add(
                    new Paragraph(
                            "Net Pay : ₹"
                                    + payslip.getNetPay()));

            document.add(
                    new Paragraph(
                            "Status : "
                                    + payslip.getStatus()));

            document.close();

            return out.toByteArray();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to generate PDF",
                    ex);
        }
    }
}