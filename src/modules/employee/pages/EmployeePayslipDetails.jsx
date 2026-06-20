import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosClient from "../../../api/axiosClient";

function EmployeePayslipDetails() {
  const { id } = useParams();

  const [payslip, setPayslip] = useState(null);

  useEffect(() => {
    loadPayslip();
  }, []);

  const loadPayslip = async () => {
    try {
      const response = await axiosClient.get(`/getpayslip/${id}`);

      setPayslip(response.data);
    } catch (error) {
      console.error(error);
      alert("Failed to load payslip");
    }
  };

  if (!payslip) {
    return <div className="container p-4">Loading Payslip...</div>;
  }

  return (
    <div
      className="container-fluid p-3"
      style={{
        maxWidth: "1100px",
        fontSize: "13px",
      }}
    >
      <div className="card shadow">
        <div className="card-body">
          {/* Header */}

          <div className="text-center mb-3">
            <h2 className="fw-bold text-primary mb-1">
              Employee Management System
            </h2>

            <h4 className="text-secondary">Salary Payslip</h4>

            <hr />
          </div>

          {/* Company Details + Employee Details */}

          <div className="row mb-3">
            <div className="col-md-6">
              <table className="table table-bordered table-sm">
                <thead className="table-primary">
                  <tr>
                    <th colSpan="2">Company Details</th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td>Company</td>
                    <td>EMS Technologies Pvt Ltd</td>
                  </tr>

                  <tr>
                    <td>Location</td>
                    <td>Hyderabad</td>
                  </tr>

                  <tr>
                    <td>Email</td>
                    <td>hr@ems.com</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="col-md-6">
              <table className="table table-bordered table-sm">
                <thead className="table-success">
                  <tr>
                    <th colSpan="2">Employee Details</th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td>Name</td>
                    <td>{payslip.employee?.employeename}</td>
                  </tr>

                  <tr>
                    <td>Employee ID</td>
                    <td>{payslip.employee?.employeeid}</td>
                  </tr>

                  <tr>
                    <td>Employee Code</td>
                    <td>{payslip.employee?.employeeCode || "N/A"}</td>
                  </tr>

                  <tr>
                    <td>Department</td>
                    <td>{payslip.employee?.department}</td>
                  </tr>

                  <tr>
                    <td>Designation</td>
                    <td>{payslip.employee?.designation}</td>
                  </tr>

                  <tr>
                    <td>Location</td>
                    <td>{payslip.employee?.workLocation}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          {/* Payslip Information */}

          <table className="table table-bordered table-sm mb-3">
            <tbody>
              <tr>
                <th>Payslip No</th>
                <td>{payslip.payslipNumber}</td>

                <th>Status</th>
                <td>{payslip.status}</td>
              </tr>

              <tr>
                <th>Month</th>
                <td>
                  {payslip.month}/{payslip.year}
                </td>

                <th>Pay Period</th>
                <td>
                  {payslip.payPeriodStart} to {payslip.payPeriodEnd}
                </td>
              </tr>
            </tbody>
          </table>

          {/* Earnings + Deductions */}

          <div className="row">
            <div className="col-md-6">
              <table className="table table-bordered table-sm">
                <thead className="table-success">
                  <tr>
                    <th>Earnings</th>
                    <th>Amount</th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td>Basic Salary</td>
                    <td>₹{payslip.basicSalary}</td>
                  </tr>

                  <tr>
                    <td>HRA</td>
                    <td>₹{payslip.hra}</td>
                  </tr>

                  <tr>
                    <td>Allowances</td>
                    <td>₹{payslip.allowances}</td>
                  </tr>

                  <tr>
                    <td>Bonus</td>
                    <td>₹{payslip.bonus}</td>
                  </tr>

                  <tr className="fw-bold table-success">
                    <td>Gross Earnings</td>
                    <td>₹{payslip.grossEarnings}</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="col-md-6">
              <table className="table table-bordered table-sm">
                <thead className="table-danger">
                  <tr>
                    <th>Deductions</th>
                    <th>Amount</th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td>PF</td>
                    <td>₹{payslip.pf}</td>
                  </tr>

                  <tr>
                    <td>ESI</td>
                    <td>₹{payslip.esi}</td>
                  </tr>

                  <tr>
                    <td>Professional Tax</td>
                    <td>₹{payslip.professionalTax}</td>
                  </tr>

                  <tr>
                    <td>Income Tax</td>
                    <td>₹{payslip.incomeTax}</td>
                  </tr>

                  <tr>
                    <td>LOP Deduction</td>
                    <td>₹{payslip.lopDeduction}</td>
                  </tr>

                  <tr>
                    <td>Other Deductions</td>
                    <td>₹{payslip.otherDeductions}</td>
                  </tr>

                  <tr className="fw-bold table-danger">
                    <td>Total Deductions</td>
                    <td>₹{payslip.totalDeductions}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          {/* Attendance + Bank Details */}

          <div className="row">
            <div className="col-md-6">
              <table className="table table-bordered table-sm">
                <thead className="table-info">
                  <tr>
                    <th colSpan="2">Attendance Summary</th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td>Working Days</td>
                    <td>{payslip.workingDays}</td>
                  </tr>

                  <tr>
                    <td>Paid Days</td>
                    <td>{payslip.paidDays}</td>
                  </tr>

                  <tr>
                    <td>LOP Days</td>
                    <td>{payslip.lopDays}</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="col-md-6">
              <table className="table table-bordered table-sm">
                <thead className="table-warning">
                  <tr>
                    <th colSpan="2">Bank Details</th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td>Bank Name</td>
                    <td>{payslip.employee?.bankName}</td>
                  </tr>

                  <tr>
                    <td>Account Holder</td>
                    <td>{payslip.employee?.accountHolderName}</td>
                  </tr>

                  <tr>
                    <td>Account Number</td>
                    <td>{payslip.employee?.accountNumber}</td>
                  </tr>

                  <tr>
                    <td>IFSC Code</td>
                    <td>{payslip.employee?.ifscCode}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          {/* Net Salary */}

          <div className="alert alert-success text-center py-3 mt-3">
            <h3 className="mb-1 fw-bold">NET SALARY PAYABLE</h3>

            <h2 className="fw-bold text-success">₹{payslip.netPay}</h2>
          </div>

          {/* Signatures */}

          <div className="row mt-5">
            <div className="col-md-6 text-center">
              <hr />
              <strong>Employee Signature</strong>
            </div>

            <div className="col-md-6 text-center">
              <hr />
              <strong>HR Signature</strong>
            </div>
          </div>

          <div className="text-center mt-3">
            <small className="text-muted">
              This is a system generated payslip and does not require a physical
              signature.
            </small>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EmployeePayslipDetails;
