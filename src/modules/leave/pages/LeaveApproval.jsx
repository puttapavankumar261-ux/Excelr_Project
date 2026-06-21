import { useEffect, useMemo, useState } from "react";
import { FiCheckCircle, FiClock, FiRefreshCw, FiXCircle } from "react-icons/fi";

import {
  EmptyState,
  EnterprisePage,
  ErrorBanner,
  LoadingState,
  MetricCard,
  PageHero,
  SearchField,
  StatusBadge,
} from "../../../components/ui/EnterpriseUI";
import { formatDate } from "../../../components/ui/formatters";
import { getApiErrorMessage } from "../../../api/errorUtils";
import {
  cancelLeave,
  finalApproveLeave,
  getAllLeaves,
  hrApproveLeave,
  managerApproveLeave,
  rejectLeave,
  teamLeadApproveLeave,
} from "../services/leaveService";

const terminalStatuses = ["APPROVED", "REJECTED", "CANCELLED"];

function LeaveApproval() {
  const [leaves, setLeaves] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [actionLoadingId, setActionLoadingId] = useState(null);
  const [error, setError] = useState("");

  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const approverId = user?.id || 1;

  const loadLeaves = async () => {
    try {
      setLoading(true);
      setError("");
      const response = await getAllLeaves();
      setLeaves(response.data || []);
    } catch (loadError) {
      console.error("Error loading leaves:", loadError);
      setError(getApiErrorMessage(loadError, "Unable to load leave approvals."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLeaves();
  }, []);

  const runAction = async (leaveId, action) => {
    try {
      setActionLoadingId(leaveId);
      setError("");
      await action();
      await loadLeaves();
    } catch (actionError) {
      console.error(actionError);
      setError(getApiErrorMessage(actionError, "Leave approval action failed."));
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleReject = async (leaveId) => {
    const reason = window.prompt("Enter rejection reason");
    if (!reason?.trim()) return;
    runAction(leaveId, () => rejectLeave(leaveId, approverId, reason.trim()));
  };

  const filteredLeaves = useMemo(() => {
    const query = search.trim().toLowerCase();
    return leaves.filter((leave) =>
      [
        leave.employeeName,
        leave.employeeId,
        leave.department,
        leave.leaveType,
        leave.approvalStatus,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase()
        .includes(query),
    );
  }, [leaves, search]);

  const pendingLeaves = leaves.filter(
    (leave) => !terminalStatuses.includes(leave.approvalStatus),
  );
  const approvedLeaves = leaves.filter((leave) => leave.approvalStatus === "APPROVED");
  const rejectedLeaves = leaves.filter((leave) => leave.approvalStatus === "REJECTED");

  return (
    <EnterprisePage>
      <PageHero
        eyebrow="Leave Approval"
        title="Approval Queue"
        description="Process leave requests at each approval stage with clear status and action visibility."
        icon={FiClock}
        meta={
          <>
            <span>{pendingLeaves.length} pending requests</span>
            <span>{filteredLeaves.length} visible rows</span>
          </>
        }
        actions={
          <button
            type="button"
            className="btn btn-light"
            onClick={loadLeaves}
            disabled={loading}
          >
            <FiRefreshCw /> Refresh
          </button>
        }
      />

      <ErrorBanner message={error} onRetry={loadLeaves} />

      <section className="enterprise-grid">
        <MetricCard
          label="Pending"
          value={pendingLeaves.length}
          helper="Ready for action"
          icon={FiClock}
          tone="gold"
        />
        <MetricCard
          label="Approved"
          value={approvedLeaves.length}
          helper="Approved requests"
          icon={FiCheckCircle}
          tone="green"
        />
        <MetricCard
          label="Rejected"
          value={rejectedLeaves.length}
          helper="Rejected requests"
          icon={FiXCircle}
          tone="red"
        />
      </section>

      <section className="enterprise-panel">
        <div className="enterprise-panel-header">
          <div>
            <h2>Approval Queue</h2>
            <p>Search employees, departments, leave types, or approval statuses.</p>
          </div>
        </div>

        <div className="enterprise-toolbar">
          <SearchField
            value={search}
            onChange={setSearch}
            placeholder="Search approval queue..."
          />
        </div>

        {loading ? (
          <LoadingState label="Loading leave approvals..." />
        ) : leaves.length === 0 ? (
          <EmptyState
            title="No leave requests found"
            message="There are no approvals to process right now."
          />
        ) : (
          <div className="enterprise-table-wrap" aria-busy={!!actionLoadingId}>
            <table className="enterprise-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Employee</th>
                  <th>Department</th>
                  <th>Leave Type</th>
                  <th>Start</th>
                  <th>End</th>
                  <th>Days</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredLeaves.map((leave) => (
                  <tr key={leave.leaveId}>
                    <td>{leave.leaveId}</td>
                    <td>
                      <span className="enterprise-row-title">
                        <span className="enterprise-avatar">
                          {(leave.employeeName || "E").charAt(0).toUpperCase()}
                        </span>
                        <span>
                          <strong>{leave.employeeName || "Employee"}</strong>
                          <span className="enterprise-subtext">
                            Employee #{leave.employeeId || "-"}
                          </span>
                        </span>
                      </span>
                    </td>
                    <td>{leave.department || "-"}</td>
                    <td>{leave.leaveType || "-"}</td>
                    <td>{formatDate(leave.leaveStartDate)}</td>
                    <td>{formatDate(leave.leaveEndDate)}</td>
                    <td>{leave.leaveDays || "-"}</td>
                    <td>
                      <StatusBadge status={leave.approvalStatus} />
                    </td>
                    <td>
                      <div className="enterprise-actions">
                        <button
                          type="button"
                          className="btn btn-outline-primary btn-sm"
                          onClick={() =>
                            runAction(leave.leaveId, () =>
                              teamLeadApproveLeave(leave.leaveId, approverId),
                            )
                          }
                          disabled={actionLoadingId === leave.leaveId}
                        >
                          Team Lead
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-warning btn-sm"
                          onClick={() =>
                            runAction(leave.leaveId, () =>
                              managerApproveLeave(leave.leaveId, approverId),
                            )
                          }
                          disabled={actionLoadingId === leave.leaveId}
                        >
                          Manager
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-info btn-sm"
                          onClick={() =>
                            runAction(leave.leaveId, () =>
                              hrApproveLeave(leave.leaveId, approverId),
                            )
                          }
                          disabled={actionLoadingId === leave.leaveId}
                        >
                          HR
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-success btn-sm"
                          onClick={() =>
                            runAction(leave.leaveId, () =>
                              finalApproveLeave(leave.leaveId),
                            )
                          }
                          disabled={actionLoadingId === leave.leaveId}
                        >
                          Final
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-danger btn-sm"
                          onClick={() => handleReject(leave.leaveId)}
                          disabled={actionLoadingId === leave.leaveId}
                        >
                          Reject
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-secondary btn-sm"
                          onClick={() =>
                            window.confirm("Cancel this leave?") &&
                            runAction(leave.leaveId, () => cancelLeave(leave.leaveId))
                          }
                          disabled={actionLoadingId === leave.leaveId}
                        >
                          Cancel
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </EnterprisePage>
  );
}

export default LeaveApproval;
