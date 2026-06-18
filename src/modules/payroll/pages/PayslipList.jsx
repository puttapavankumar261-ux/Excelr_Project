import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getAllPayslips,
  approvePayslip,
  markPayslipPaid,
  deletePayslip,
} from "../services/payslipService";

function PayslipList() {
  const [payslips, setPayslips] = useState([]);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    loadPayslips();
  }, []);

  const loadPayslips = async () => {
    try {
      setLoading(true);

      const response = await getAllPayslips();

      setPayslips(response.data || []);
    } catch (error) {
      console.error(error);

      alert(error.response?.data?.message || "Failed to load payslips");
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (payslipId) => {
    try {
      await approvePayslip(payslipId, 1);

      alert("Payslip Approved");

      loadPayslips();
    } catch (error) {
      alert(error.response?.data?.message || "Failed to approve payslip");
    }
  };

  const handleMarkPaid = async (payslipId) => {
    try {
      await markPayslipPaid(payslipId);

      alert("Payslip marked as Paid");

      loadPayslips();
    } catch (error) {
      alert(error.response?.data?.message || "Failed to mark paid");
    }
  };

  const handleDelete = async (payslipId) => {
    if (!window.confirm("Delete Payslip?")) {
      return;
    }

    try {
      await deletePayslip(payslipId);

      alert("Payslip deleted");

      loadPayslips();
    } catch (error) {
      alert(error.response?.data?.message || "Failed to delete payslip");
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "GENERATED":
        return <span className="badge bg-warning text-dark">Generated</span>;

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
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Payslip Management</h2>

        <button className="btn btn-primary" onClick={loadPayslips}>
          Refresh
        </button>
      </div>

      {loading ? (
        <div className="text-center">Loading Payslips...</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-bordered table-hover align-middle">
            <thead className="table-dark">
              <tr>
                <th>ID</th>
                <th>Employee</th>
                <th>Month</th>
                <th>Gross Earnings</th>
                <th>Deductions</th>
                <th>Net Pay</th>
                <th>Status</th>
                <th style={{ minWidth: "400px" }}>Actions</th>
              </tr>
            </thead>

            <tbody>
              {payslips.length > 0 ? (
                payslips.map((payslip) => (
                  <tr key={payslip.payslipId}>
                    <td>{payslip.payslipId}</td>

                    <td>{payslip.employee?.employeename}</td>

                    <td>
                      {payslip.month}/{payslip.year}
                    </td>

                    <td>₹{payslip.grossEarnings}</td>

                    <td>₹{payslip.totalDeductions}</td>

                    <td>₹{payslip.netPay}</td>

                    <td>{getStatusBadge(payslip.status)}</td>

                    <td>
                      <div className="d-flex flex-wrap gap-2">
                        <button
                          className="btn btn-info btn-sm"
                          onClick={() =>
                            navigate(
                              `/admin/payslips/view/${payslip.payslipId}`,
                            )
                          }
                        >
                          View
                        </button>

                        <button
                          className="btn btn-warning btn-sm"
                          onClick={() =>
                            navigate(
                              `/admin/payslips/edit/${payslip.payslipId}`,
                            )
                          }
                          disabled={payslip.status === "PAID"}
                        >
                          Edit
                        </button>

                        <button
                          className="btn btn-success btn-sm"
                          onClick={() => handleApprove(payslip.payslipId)}
                          disabled={
                            payslip.status === "HR_APPROVED" ||
                            payslip.status === "PAID"
                          }
                        >
                          Approve
                        </button>

                        <button
                          className="btn btn-primary btn-sm"
                          onClick={() => handleMarkPaid(payslip.payslipId)}
                          disabled={payslip.status === "PAID"}
                        >
                          Paid
                        </button>

                        <button className="btn btn-secondary btn-sm">
                          Download
                        </button>

                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleDelete(payslip.payslipId)}
                        >
                          Delete
                        </button>
                      </div>
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
      )}
    </div>
  );
}

export default PayslipList;
