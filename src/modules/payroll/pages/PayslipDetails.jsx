import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosClient from "../../../api/axiosClient";

function PayslipDetails() {
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
    }
  };

  const handlePrint = () => {
    window.print();
  };

  if (!payslip) {
    return <h4>Loading...</h4>;
  }

  return (
    <div className="container p-4" id="payslip-content">
      <div className="d-flex justify-content-between mb-4">
        <div>
          <h2>ABC Technologies Pvt Ltd</h2>
          <p>
            Hyderabad, Telangana, India
            <br />
            payroll@abctech.com
          </p>
        </div>

        <button className="btn btn-success" onClick={handlePrint}>
          Download / Print
        </button>
      </div>

      <h3 className="text-center mb-4">Salary Payslip</h3>

      <table className="table table-bordered">
        <tbody>
          <tr>
            <th>Payslip Number</th>
            <td>{payslip.payslipNumber}</td>

            <th>Status</th>
            <td>{payslip.status}</td>
          </tr>

          <tr>
            <th>Pay Period</th>
            <td>
              {payslip.month}/{payslip.year}
            </td>

            <th>Employee ID</th>
            <td>{payslip.employee?.employeeid}</td>
          </tr>
        </tbody>
      </table>

      <h4>Employee Information</h4>

      <table className="table table-bordered">
        <tbody>
          <tr>
            <th>Name</th>
            <td>{payslip.employee?.employeename}</td>

            <th>Department</th>
            <td>{payslip.employee?.department}</td>
          </tr>

          <tr>
            <th>Designation</th>
            <td>{payslip.employee?.designation}</td>

            <th>Location</th>
            <td>{payslip.employee?.workLocation}</td>
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

          <tr>
            <th>Gross Earnings</th>
            <td>
              <strong>₹{payslip.grossEarnings}</strong>
            </td>
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
            <th>Total Deductions</th>
            <td>
              <strong>₹{payslip.totalDeductions}</strong>
            </td>
          </tr>
        </tbody>
      </table>

      <h4>Bank Transfer Details</h4>

      <table className="table table-bordered">
        <tbody>
          <tr>
            <th>Bank Name</th>
            <td>{payslip.employee?.bankName || "-"}</td>
          </tr>

          <tr>
            <th>Account Holder</th>
            <td>{payslip.employee?.accountHolderName || "-"}</td>
          </tr>

          <tr>
            <th>Account Number</th>
            <td>{payslip.employee?.accountNumber || "-"}</td>
          </tr>

          <tr>
            <th>IFSC Code</th>
            <td>{payslip.employee?.ifscCode || "-"}</td>
          </tr>
        </tbody>
      </table>

      <div className="alert alert-success text-center">
        <h4>Net Pay : ₹{payslip.netPay}</h4>
      </div>

      <p className="text-center mt-4">
        This is a system-generated payslip and does not require a signature.
      </p>
    </div>
  );
}

export default PayslipDetails;
