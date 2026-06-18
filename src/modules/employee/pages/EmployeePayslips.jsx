import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getEmployeePayslips } from "../../payroll/services/payslipService";

function EmployeePayslips() {
  const [payslips, setPayslips] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    loadPayslips();
  }, []);

  const loadPayslips = async () => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));

      console.log("Logged User:", user);

      const employeeId = user?.id || user?.employeeId || user?.employeeid;

      if (!employeeId) {
        alert("Employee ID not found.");
        return;
      }

      const response = await getEmployeePayslips(employeeId);

      console.log("Employee Payslips:", response.data);

      setPayslips(response.data || []);
    } catch (error) {
      console.error(error);

      alert(error.response?.data?.message || "Failed to load payslips");
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "GENERATED":
        return <span className="badge bg-warning">Generated</span>;

      case "HR_APPROVED":
        return <span className="badge bg-info">Approved</span>;

      case "PAID":
        return <span className="badge bg-success">Paid</span>;

      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  return (
    <div className="container-fluid p-4">
      <h2 className="mb-4">My Payslips</h2>

      <table className="table table-bordered table-hover">
        <thead className="table-dark">
          <tr>
            <th>Payslip No</th>
            <th>Year</th>
            <th>Month</th>
            <th>Gross Earnings</th>
            <th>Deductions</th>
            <th>Net Pay</th>
            <th>Status</th>
            <th width="220">Actions</th>
          </tr>
        </thead>

        <tbody>
          {payslips.length > 0 ? (
            payslips.map((payslip) => (
              <tr key={payslip.payslipId}>
                <td>{payslip.payslipNumber}</td>

                <td>{payslip.year}</td>

                <td>{payslip.month}</td>

                <td>₹{payslip.grossEarnings}</td>

                <td>₹{payslip.totalDeductions}</td>

                <td>₹{payslip.netPay}</td>

                <td>{getStatusBadge(payslip.status)}</td>

                <td>
                  <button
                    className="btn btn-info btn-sm me-2"
                    onClick={() =>
                      navigate(`/employee/payslips/view/${payslip.payslipId}`)
                    }
                  >
                    View
                  </button>

                  <button
                    className="btn btn-success btn-sm"
                    onClick={() => window.print()}
                  >
                    Download
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="8" className="text-center">
                No Payslips Found
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default EmployeePayslips;
