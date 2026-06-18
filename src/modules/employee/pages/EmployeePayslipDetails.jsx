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
    <div className="container p-4">
      <div id="payslip-content" className="card shadow">
        <div className="card-body">
          <div className="text-center mb-4">
            <h2>Employee Payslip</h2>

            <h5>Payslip Number : {payslip.payslipNumber}</h5>

            <p>
              Pay Period : {payslip.payPeriodStart} to {payslip.payPeriodEnd}
            </p>
          </div>

          <hr />

          <h4>Employee Information</h4>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th>Employee Name</th>
                <td>{payslip.employee?.employeename}</td>

                <th>Employee ID</th>
                <td>{payslip.employee?.employeeid}</td>
              </tr>

              <tr>
                <th>Department</th>
                <td>{payslip.employee?.department}</td>

                <th>Designation</th>
                <td>{payslip.employee?.designation}</td>
              </tr>

              <tr>
                <th>Location</th>
                <td>{payslip.employee?.workLocation}</td>

                <th>Status</th>
                <td>{payslip.status}</td>
              </tr>
            </tbody>
          </table>

          <h4>Earnings</h4>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th>Basic Salary</th>
                <td>₹{payslip.basicSalary}</td>
              </tr>

              <tr>
                <th>HRA</th>
                <td>₹{payslip.hra}</td>
              </tr>

              <tr>
                <th>Allowances</th>
                <td>₹{payslip.allowances}</td>
              </tr>

              <tr>
                <th>Bonus</th>
                <td>₹{payslip.bonus}</td>
              </tr>

              <tr className="table-success">
                <th>Gross Earnings</th>
                <td>₹{payslip.grossEarnings}</td>
              </tr>
            </tbody>
          </table>

          <h4>Deductions</h4>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th>PF</th>
                <td>₹{payslip.pf}</td>
              </tr>

              <tr>
                <th>ESI</th>
                <td>₹{payslip.esi}</td>
              </tr>

              <tr>
                <th>Professional Tax</th>
                <td>₹{payslip.professionalTax}</td>
              </tr>

              <tr>
                <th>Income Tax</th>
                <td>₹{payslip.incomeTax}</td>
              </tr>

              <tr>
                <th>LOP Deduction</th>
                <td>₹{payslip.lopDeduction}</td>
              </tr>

              <tr>
                <th>Other Deductions</th>
                <td>₹{payslip.otherDeductions}</td>
              </tr>

              <tr className="table-danger">
                <th>Total Deductions</th>
                <td>₹{payslip.totalDeductions}</td>
              </tr>
            </tbody>
          </table>

          <h4>Attendance Summary</h4>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th>Working Days</th>
                <td>{payslip.workingDays}</td>

                <th>Paid Days</th>
                <td>{payslip.paidDays}</td>
              </tr>

              <tr>
                <th>LOP Days</th>
                <td>{payslip.lopDays}</td>

                <th>Net Pay</th>
                <td>₹{payslip.netPay}</td>
              </tr>
            </tbody>
          </table>

          <div className="alert alert-success text-center mt-4">
            <h3>Net Salary Payable : ₹{payslip.netPay}</h3>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EmployeePayslipDetails;
