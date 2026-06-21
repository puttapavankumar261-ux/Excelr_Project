import { useEffect, useMemo, useState } from "react";
import {
  FiCheckCircle,
  FiDollarSign,
  FiPlus,
  FiRefreshCw,
  FiTrash2,
} from "react-icons/fi";

import {
  EmptyState,
  EnterprisePage,
  ErrorBanner,
  LoadingState,
  MetricCard,
  MiniBarChart,
  PageHero,
  SearchField,
  StatusBadge,
} from "../../../components/ui/EnterpriseUI";
import { formatCurrency, formatDate } from "../../../components/ui/formatters";
import { getApiErrorMessage } from "../../../api/errorUtils";
import {
  approvePayroll,
  deletePayroll,
  getAllPayrolls,
} from "../services/payrollService";
import AddPayrollModal from "../components/AddPayrollModal";

const getEmployeeName = (payroll) =>
  payroll?.employee?.employeename ||
  payroll?.employeeName ||
  payroll?.employeename ||
  "Employee";

function Payroll() {
  const [payrolls, setPayrolls] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [loading, setLoading] = useState(true);
  const [actionLoadingId, setActionLoadingId] = useState(null);
  const [error, setError] = useState("");

  const loadPayrolls = async () => {
    try {
      setLoading(true);
      setError("");
      const response = await getAllPayrolls();
      setPayrolls(response.data || []);
    } catch (loadError) {
      console.error(loadError);
      setError(getApiErrorMessage(loadError, "Unable to load payroll records."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPayrolls();
  }, []);

  const filteredPayrolls = useMemo(() => {
    const query = search.trim().toLowerCase();

    return payrolls.filter((payroll) => {
      const searchable = [
        getEmployeeName(payroll),
        payroll?.employee?.department,
        payroll.payrollMonth,
        payroll.status,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      const searchMatch = !query || searchable.includes(query);
      const statusMatch = statusFilter === "ALL" || payroll.status === statusFilter;

      return searchMatch && statusMatch;
    });
  }, [payrolls, search, statusFilter]);

  const totals = useMemo(() => {
    return payrolls.reduce(
      (acc, payroll) => {
        acc.gross += Number(payroll.grossSalary || 0);
        acc.net += Number(payroll.netSalary || 0);
        acc.statuses[payroll.status || "UNKNOWN"] =
          (acc.statuses[payroll.status || "UNKNOWN"] || 0) + 1;
        return acc;
      },
      { gross: 0, net: 0, statuses: {} },
    );
  }, [payrolls]);

  const approvedCount = payrolls.filter(
    (payroll) => payroll.status === "APPROVED",
  ).length;
  const pendingCount = payrolls.length - approvedCount;

  const handleApprove = async (id) => {
    try {
      setActionLoadingId(id);
      setError("");
      await approvePayroll(id, 1);
      await loadPayrolls();
    } catch (approveError) {
      console.error(approveError);
      setError(getApiErrorMessage(approveError, "Payroll approval failed."));
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this payroll record?")) return;

    try {
      setActionLoadingId(id);
      setError("");
      await deletePayroll(id);
      setPayrolls((current) => current.filter((payroll) => payroll.payrollId !== id));
    } catch (deleteError) {
      console.error(deleteError);
      setError(getApiErrorMessage(deleteError, "Payroll delete failed."));
    } finally {
      setActionLoadingId(null);
    }
  };

  const statusChart = Object.entries(totals.statuses).map(([label, value]) => ({
    label,
    value,
  }));

  return (
    <EnterprisePage>
      <PageHero
        eyebrow="Payroll Management"
        title="Payroll Processing"
        description="Generate payroll, monitor approval status, and review monthly compensation totals in a production-ready workflow."
        icon={FiDollarSign}
        meta={
          <>
            <span>{filteredPayrolls.length} visible payrolls</span>
            <span>{formatCurrency(totals.net)} net payable</span>
          </>
        }
        actions={
          <>
            <button
              type="button"
              className="btn btn-light"
              onClick={loadPayrolls}
              disabled={loading}
            >
              <FiRefreshCw /> Refresh
            </button>
            <button
              type="button"
              className="btn btn-warning"
              onClick={() => setShowModal(true)}
            >
              <FiPlus /> Generate Payroll
            </button>
          </>
        }
      />

      <ErrorBanner message={error} onRetry={loadPayrolls} />

      <section className="enterprise-grid">
        <MetricCard
          label="Payroll Runs"
          value={payrolls.length}
          helper="Total payroll records"
          icon={FiDollarSign}
          tone="blue"
        />
        <MetricCard
          label="Gross Payroll"
          value={formatCurrency(totals.gross)}
          helper="Before deductions"
          icon={FiDollarSign}
          tone="gold"
        />
        <MetricCard
          label="Net Payable"
          value={formatCurrency(totals.net)}
          helper="After deductions"
          icon={FiCheckCircle}
          tone="green"
        />
        <MetricCard
          label="Pending Approval"
          value={pendingCount}
          helper={`${approvedCount} approved`}
          icon={FiRefreshCw}
          tone="red"
        />
      </section>

      <section className="enterprise-panel">
        <div className="enterprise-panel-header">
          <div>
            <h2>Payroll Register</h2>
            <p>Search by employee, month, department, or status.</p>
          </div>
        </div>

        <div className="enterprise-toolbar">
          <SearchField
            value={search}
            onChange={setSearch}
            placeholder="Search payroll records..."
          />
          <select
            className="form-select"
            value={statusFilter}
            onChange={(event) => setStatusFilter(event.target.value)}
            aria-label="Filter payroll by status"
          >
            <option value="ALL">All Statuses</option>
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
          </select>
        </div>

        {loading ? (
          <LoadingState label="Loading payrolls..." />
        ) : payrolls.length === 0 ? (
          <EmptyState
            title="No payroll records"
            message="Generate payroll to populate the payroll register."
          />
        ) : (
          <div className="enterprise-table-wrap" aria-busy={!!actionLoadingId}>
            <table className="enterprise-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Employee</th>
                  <th>Month</th>
                  <th>Gross</th>
                  <th>Net</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredPayrolls.length > 0 ? (
                  filteredPayrolls.map((payroll) => (
                    <tr key={payroll.payrollId}>
                      <td>{payroll.payrollId}</td>
                      <td>
                        <span className="enterprise-row-title">
                          <span className="enterprise-avatar">
                            {getEmployeeName(payroll).charAt(0).toUpperCase()}
                          </span>
                          <span>
                            <strong>{getEmployeeName(payroll)}</strong>
                            <span className="enterprise-subtext">
                              {payroll?.employee?.department || "Payroll employee"}
                            </span>
                          </span>
                        </span>
                      </td>
                      <td>{formatDate(payroll.payrollMonth)}</td>
                      <td>{formatCurrency(payroll.grossSalary)}</td>
                      <td>{formatCurrency(payroll.netSalary)}</td>
                      <td>
                        <StatusBadge status={payroll.status || "PENDING"} />
                      </td>
                      <td>
                        <div className="enterprise-actions">
                          {payroll.status !== "APPROVED" && (
                            <button
                              type="button"
                              className="btn btn-outline-primary btn-sm"
                              onClick={() => handleApprove(payroll.payrollId)}
                              disabled={actionLoadingId === payroll.payrollId}
                            >
                              <FiCheckCircle /> Approve
                            </button>
                          )}
                          <button
                            type="button"
                            className="btn btn-outline-danger btn-sm"
                            onClick={() => handleDelete(payroll.payrollId)}
                            disabled={actionLoadingId === payroll.payrollId}
                          >
                            <FiTrash2 /> Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="7">
                      <EmptyState
                        title="No matching payroll"
                        message="Try changing the search or status filter."
                      />
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </section>

      {statusChart.length > 0 && (
        <section className="enterprise-panel">
          <div className="enterprise-panel-header">
            <div>
              <h3>Approval Status</h3>
              <p>Current payroll workflow distribution.</p>
            </div>
          </div>
          <MiniBarChart items={statusChart} valueLabel="payrolls" />
        </section>
      )}

      {showModal && (
        <AddPayrollModal
          onClose={() => setShowModal(false)}
          onSuccess={loadPayrolls}
        />
      )}
    </EnterprisePage>
  );
}

export default Payroll;
