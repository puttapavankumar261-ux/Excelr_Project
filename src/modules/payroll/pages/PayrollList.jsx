import { useEffect, useState } from "react";

import AddPayrollModal from "../components/AddPayrollModal";

import {
  getAllPayrolls,
  approvePayroll,
  deletePayroll,
} from "../services/payrollService";

function PayrollList() {
  const [payrolls, setPayrolls] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    loadPayrolls();
  }, []);

  const loadPayrolls = async () => {
    try {
      setLoading(true);

      const response = await getAllPayrolls();

      setPayrolls(response.data || []);
    } catch (error) {
      console.error(error);

      alert(error.response?.data?.message || "Failed to load payrolls");
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (payrollId) => {
    try {
      await approvePayroll(payrollId, 1);

      alert("Payroll approved successfully");

      loadPayrolls();
    } catch (error) {
      alert(error.response?.data?.message || "Failed to approve payroll");
    }
  };

  const handleDelete = async (payrollId) => {
    if (!window.confirm("Delete payroll record?")) return;

    try {
      await deletePayroll(payrollId);

      alert("Payroll deleted successfully");

      loadPayrolls();
    } catch (error) {
      alert(error.response?.data?.message || "Failed to delete payroll");
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "HR_APPROVED":
        return <span className="badge bg-success">Approved</span>;

      case "PAID":
        return <span className="badge bg-primary">Paid</span>;

      default:
        return <span className="badge bg-warning text-dark">Draft</span>;
    }
  };

  return (
    <div className="container-fluid p-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2 className="mb-0">Payroll Management</h2>

        <div>
          <button className="btn btn-secondary me-2" onClick={loadPayrolls}>
            Refresh
          </button>

          <button
            className="btn btn-primary"
            onClick={() => setShowModal(true)}
          >
            Create Payroll
          </button>
        </div>
      </div>

      {loading ? (
        <div className="text-center">Loading payrolls...</div>
      ) : (
        <table className="table table-bordered table-hover">
          <thead className="table-dark">
            <tr>
              <th>#</th>
              <th>Payroll ID</th>
              <th>Employee</th>
              <th>Gross Salary</th>
              <th>Deductions</th>
              <th>Net Salary</th>
              <th>Status</th>
              <th>Payroll Month</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {payrolls.length > 0 ? (
              payrolls.map((payroll, index) => (
                <tr key={payroll.payrollId}>
                  <td>{index + 1}</td>

                  <td>{payroll.payrollId}</td>

                  <td>{payroll.employee?.employeename}</td>

                  <td>₹{payroll.grossSalary}</td>

                  <td>₹{payroll.deductions}</td>

                  <td>₹{payroll.netSalary}</td>

                  <td>{getStatusBadge(payroll.status)}</td>

                  <td>{payroll.payrollMonth}</td>

                  <td>
                    <button
                      className="btn btn-success btn-sm me-2"
                      disabled={payroll.status === "HR_APPROVED"}
                      onClick={() => handleApprove(payroll.payrollId)}
                    >
                      Approve
                    </button>

                    <button
                      className="btn btn-danger btn-sm"
                      onClick={() => handleDelete(payroll.payrollId)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="9" className="text-center">
                  No Payroll Records Found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}

      {showModal && (
        <AddPayrollModal
          onClose={() => setShowModal(false)}
          onSuccess={loadPayrolls}
        />
      )}
    </div>
  );
}

export default PayrollList;
