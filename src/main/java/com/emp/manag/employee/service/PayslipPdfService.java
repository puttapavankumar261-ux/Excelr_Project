package com.emp.manag.employee.service;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.manag.employee.entity.EmpEntity;
import com.emp.manag.employee.entity.PayslipEntity;
import com.lowagie.text.Rectangle;
import com.lowagie.text.PageSize;
import com.lowagie.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PayslipPdfService {

    @Autowired
    private PayslipService payslipService;
    
    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell createNoBorderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(4);
        return cell;
    }

    public byte[] generatePdf(Integer payslipId) {

        try {

            PayslipEntity payslip =
                    payslipService.getPayslipById(payslipId);

            EmpEntity employee =
                    payslip.getEmployee();

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            Document document =
                    new Document(
                            PageSize.A4,
                            30,
                            30,
                            30,
                            30);

            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            18);

            Font sectionFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            12);

            Font netPayFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            16);
            
            Paragraph title =
                    new Paragraph(
                            "EMS TECHNOLOGIES PVT LTD",
                            titleFont);

            title.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(title);

            Paragraph address =
                    new Paragraph(
                            "Hyderabad, Telangana",
                            FontFactory.getFont(
                                    FontFactory.HELVETICA,
                                    10));

            address.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(address);

            Paragraph email =
                    new Paragraph(
                            "payroll@ems.com",
                            FontFactory.getFont(
                                    FontFactory.HELVETICA,
                                    10));

            email.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(email);

            document.add(
                    new Paragraph(" "));

            Paragraph payslipTitle =
                    new Paragraph(
                            "SALARY PAYSLIP",
                            titleFont);

            payslipTitle.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(payslipTitle);

            document.add(
                    new Paragraph(" "));

            PdfPTable payslipInfo =
                    new PdfPTable(4);

            payslipInfo.setWidthPercentage(100);

            payslipInfo.addCell("Payslip No");
            payslipInfo.addCell(
                    payslip.getPayslipNumber());

            payslipInfo.addCell("Status");
            payslipInfo.addCell(
                    String.valueOf(
                            payslip.getStatus()));

            payslipInfo.addCell("Month");
            payslipInfo.addCell(
                    payslip.getMonth()
                    + "/"
                    + payslip.getYear());

            payslipInfo.addCell("Working Days");
            payslipInfo.addCell(
                    String.valueOf(
                            payslip.getWorkingDays()));

            payslipInfo.addCell("Paid Days");
            payslipInfo.addCell(
                    String.valueOf(
                            payslip.getPaidDays()));

            payslipInfo.addCell("LOP Days");
            payslipInfo.addCell(
                    String.valueOf(
                            payslip.getLopDays()));

            document.add(payslipInfo);

            document.add(
                    new Paragraph(" "));

            // Earnings Table

            document.add(
                    new Paragraph(
                            "Earnings",
                            sectionFont));

            PdfPTable earningsTable =
                    new PdfPTable(2);

            earningsTable.setWidthPercentage(100);

            earningsTable.addCell("Component");
            earningsTable.addCell("Amount");

            earningsTable.addCell("Basic Salary");
            earningsTable.addCell(
                    "₹" + payslip.getBasicSalary());

            earningsTable.addCell("HRA");
            earningsTable.addCell(
                    "₹" + payslip.getHra());

            earningsTable.addCell("Allowances");
            earningsTable.addCell(
                    "₹" + payslip.getAllowances());

            earningsTable.addCell("Bonus");
            earningsTable.addCell(
                    "₹" + payslip.getBonus());

            earningsTable.addCell("Gross Earnings");
            earningsTable.addCell(
                    "₹" + payslip.getGrossEarnings());

            document.add(earningsTable);

            document.add(
                    new Paragraph(" "));

            // Deductions Table

            document.add(
                    new Paragraph(
                            "Deductions",
                            sectionFont));

            PdfPTable deductionTable =
                    new PdfPTable(2);

            deductionTable.setWidthPercentage(100);

            deductionTable.addCell("Component");
            deductionTable.addCell("Amount");

            deductionTable.addCell("PF");
            deductionTable.addCell(
                    "₹" + payslip.getPf());

            deductionTable.addCell("ESI");
            deductionTable.addCell(
                    "₹" + payslip.getEsi());

            deductionTable.addCell("Professional Tax");
            deductionTable.addCell(
                    "₹" + payslip.getProfessionalTax());

            deductionTable.addCell("Income Tax");
            deductionTable.addCell(
                    "₹" + payslip.getIncomeTax());

            deductionTable.addCell("LOP Deduction");
            deductionTable.addCell(
                    "₹" + payslip.getLopDeduction());
            
            deductionTable.addCell(
                    "Other Deductions");

            deductionTable.addCell(
                    "₹"
                    + payslip.getOtherDeductions());

            deductionTable.addCell("Total Deductions");
            deductionTable.addCell(
                    "₹" + payslip.getTotalDeductions());

            document.add(deductionTable);

            document.add(
                    new Paragraph(" "));
            
            PdfPTable attendance =
                    new PdfPTable(2);

            attendance.setWidthPercentage(100);

            attendance.addCell(
                    "Working Days");

            attendance.addCell(
                    String.valueOf(
                            payslip.getWorkingDays()));

            attendance.addCell(
                    "Paid Days");

            attendance.addCell(
                    String.valueOf(
                            payslip.getPaidDays()));

            attendance.addCell(
                    "LOP Days");

            attendance.addCell(
                    String.valueOf(
                            payslip.getLopDays()));

            document.add(
                    new Paragraph(" "));

            document.add(attendance);

            
            PdfPTable bank =
                    new PdfPTable(2);

            bank.setWidthPercentage(100);

            bank.addCell(
                    "Bank Name");

            bank.addCell(
                    String.valueOf(
                            employee.getBankName()));

            bank.addCell(
                    "Account Holder");

            bank.addCell(
                    String.valueOf(
                            employee.getAccountHolderName()));

            bank.addCell(
                    "Account Number");

            bank.addCell(
                    String.valueOf(
                            employee.getAccountNumber()));

            bank.addCell(
                    "IFSC Code");

            bank.addCell(
                    String.valueOf(
                            employee.getIfscCode()));

            document.add(
                    new Paragraph(" "));

            document.add(bank);
            
            PdfPTable summary =
                    new PdfPTable(2);

            summary.setWidthPercentage(100);

            summary.addCell(
                    "Gross Earnings");

            summary.addCell(
                    "₹"
                    + payslip.getGrossEarnings());

            summary.addCell(
                    "Total Deductions");

            summary.addCell(
                    "₹"
                    + payslip.getTotalDeductions());

            document.add(
                    new Paragraph(" "));

            document.add(summary);
            
            // Net Pay

            PdfPTable netPayTable =
                    new PdfPTable(1);

            netPayTable.setWidthPercentage(
                    100);

            PdfPCell netCell =
                    new PdfPCell(
                            new Phrase(
                                    "NET PAY : ₹"
                                            + payslip.getNetPay(),
                                    netPayFont));

            netCell.setHorizontalAlignment(
                    Element.ALIGN_CENTER);

            netCell.setPadding(12);

            netPayTable.addCell(
                    netCell);

            document.add(
                    new Paragraph(" "));

            document.add(
                    netPayTable);
            
            document.add(
                    new Paragraph(" "));

            PdfPTable signTable =
                    new PdfPTable(2);

            signTable.setWidthPercentage(100);

            signTable.addCell(
                    createNoBorderCell(
                            "Employee Signature",
                            sectionFont));

            signTable.addCell(
                    createNoBorderCell(
                            "HR Signature",
                            sectionFont));

            document.add(signTable);

            document.add(
                    new Paragraph(" "));

            Paragraph footer =
                    new Paragraph(
                            "This is a system generated payslip.",
                            FontFactory.getFont(
                                    FontFactory.HELVETICA,
                                    9));

            footer.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(footer);

            document.close();

            return out.toByteArray();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to generate PDF",
                    ex);
        }
    }
}