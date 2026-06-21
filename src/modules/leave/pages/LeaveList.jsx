import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FiCalendar,
  FiCheckCircle,
  FiRefreshCw,
  FiSend,
  FiSlash,
  FiXCircle,
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
import { formatDate } from "../../../components/ui/formatters";
import { getApiErrorMessage } from "../../../api/errorUtils";
import {
  cancelLeave,
  finalApproveLeave,
  getAllLeaves,
  hrApproveLeave,
  managerApproveLeave,
  rejectLeave,
  sendToHr,
  sendToManager,
  teamLeadApproveLeave,
} from "../services/leaveService";

const terminalStatuses = ["APPROVED", "REJECTED", "CANCELLED"];

function LeaveList() {
  const navigate = useNavigate();
  const [leaves, setLeaves] = useState([]);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [loading, setLoading] = useState(true);
  const [actionLoadingId, setActionLoadingId] = useState(null);
  const [error, setError] = useState("");
  const [rejectModal, setRejectModal] = useState({ open: false, leaveId: null });
  const [rejectReason, setRejectReason] = useState("");

  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const adminId = user?.id || 1;

  const loadLeaves = async () => {
    try {
      setLoading(true);
      setError("");
      const response = await getAllLeaves();
      setLeaves(response.data || []);
    } catch (loadError) {
      console.error("Error loading leaves", loadError);
      setError(getApiErrorMessage(loadError, "Unable to load leave requests."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLeaves();
  }, []);

  const runLeaveAction = async (leaveId, action) => {
    try {
      setActionLoadingId(leaveId);
      setError("");
      await action();
      await loadLeaves();
    } catch (actionError) {
      console.error(actionError);
      setError(getApiErrorMessage(actionError, "Leave action failed."));
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleTeamLeadApprove = (leaveId) =>
    runLeaveAction(leaveId, async () => {
      await teamLeadApproveLeave(leaveId, adminId);
      await sendToManager(leaveId);
    });

  const handleManagerApprove = (leaveId) =>
    runLeaveAction(leaveId, async () => {
      await managerApproveLeave(leaveId, adminId);
      await sendToHr(leaveId);
    });

  const handleHrApprove = (leaveId) =>
    runLeaveAction(leaveId, () => hrApproveLeave(leaveId, adminId));

  const handleFinalApprove = (leaveId) =>
    runLeaveAction(leaveId, () => finalApproveLeave(leaveId));

  const handleCancelLeave = (leaveId) => {
    if (!window.confirm("Cancel this approved leave?")) return;
    runLeaveAction(leaveId, () => cancelLeave(leaveId));
  };

  const handleRejectLeave = async () => {
    if (!rejectReason.trim()) {
      setError("Please enter a rejection reason before rejecting a leave.");
      return;
    }

    await runLeaveAction(rejectModal.leaveId, () =>
      rejectLeave(rejectModal.leaveId, adminId, rejectReason.trim()),
    );
    setRejectModal({ open: false, leaveId: null });
    setRejectReason("");
  };

  const filteredLeaves = useMemo(() => {
    const query = search.trim().toLowerCase();

    return leaves.filter((leave) => {
      const searchable = [
        leave.employeeName,
        leave.employeeId,
        leave.department,
        leave.leaveType,
        leave.approvalStatus,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      const employeeMatch = !query || searchable.includes(query);
      const statusMatch =
        statusFilter === "ALL" || leave.approvalStatus === statusFilter;

      return employeeMatch && statusMatch;
    });
  }, [leaves, search, statusFilter]);

  const counts = useMemo(() => {
    return leaves.reduce(
      (acc, leave) => {
        const status = leave.approvalStatus || "UNKNOWN";
        acc.statuses[status] = (acc.statuses[status] || 0) + 1;
        if (!terminalStatuses.includes(status)) acc.pending += 1;
        if (status === "APPROVED") acc.approved += 1;
        if (status === "REJECTED") acc.rejected += 1;
        if (status === "CANCELLED") acc.cancelled += 1;
        return acc;
      },
      { pending: 0, approved: 0, rejected: 0, cancelled: 0, statuses: {} },
    );
  }, [leaves]);

  const chartItems = Object.entries(counts.statuses)
    .map(([label, value]) => ({ label, value }))
    .sort((a, b) => b.value - a.value);

  return (
    <EnterprisePage>
      <PageHero
        eyebrow="Leave Management"
        title="Leave Requests"
        description="Review requests, move approvals through the workflow, and manage leave exceptions with clear status visibility."
        icon={FiCalendar}
        meta={
          <>
            <span>{filteredLeaves.length} visible requests</span>
            <span>{counts.pending} pending approvals</span>
          </>
        }
        actions={
          <>
            <button
              type="button"
              className="btn btn-light"
              onClick={loadLeaves}
              disabled={loading}
            >
              <FiRefreshCw /> Refresh
            </button>
            <button
              type="button"
              className="btn btn-warning"
              onClick={() => navigate("/admin/leave-approval")}
            >
              <FiSend /> Approval Console
            </button>
          </>
        }
      />

      <ErrorBanner message={error} onRetry={loadLeaves} />

      <section className="enterprise-grid">
        <MetricCard
          label="Total Leaves"
          value={leaves.length}
          helper="All leave requests"
          icon={FiCalendar}
          tone="blue"
        />
        <MetricCard
          label="Pending"
          value={counts.pending}
          helper="Awaiting workflow action"
          icon={FiSend}
          tone="gold"
        />
        <MetricCard
          label="Approved"
          value={counts.approved}
          helper={`${counts.cancelled} cancelled`}
          icon={FiCheckCircle}
          tone="green"
        />
        <MetricCard
          label="Rejected"
          value={counts.rejected}
          helper="Rejected requests"
          icon={FiXCircle}
          tone="red"
        />
      </section>

      <section className="enterprise-panel">
        <div className="enterprise-panel-header">
          <div>
            <h2>Leave Workflow</h2>
            <p>Search by employee, department, leave type, or approval status.</p>
          </div>
        </div>

        <div className="enterprise-toolbar">
          <SearchField
            value={search}
            onChange={setSearch}
            placeholder="Search leave requests..."
          />
          <select
            className="form-select"
            value={statusFilter}
            onChange={(event) => setStatusFilter(event.target.value)}
            aria-label="Filter leaves by status"
          >
            <option value="ALL">All Statuses</option>
            <option value="PENDING_TEAM_LEAD">Pending Team Lead</option>
            <option value="PENDING_MANAGER">Pending Manager</option>
            <option value="PENDING_HR">Pending HR</option>
            <option value="HR_REVIEWED">HR Reviewed</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </div>

        {loading ? (
          <LoadingState label="Loading leave requests..." />
        ) : leaves.length === 0 ? (
          <EmptyState
            title="No leave requests"
            message="Leave requests submitted by employees will appear here."
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
                {filteredLeaves.length > 0 ? (
                  filteredLeaves.map((leave) => (
                    <tr key={leave.leaveId}>
                      <td>{leave.employeeId || leave.leaveId}</td>
                      <td>
                        <span className="enterprise-row-title">
                          <span className="enterprise-avatar">
                            {(leave.employeeName || "E").charAt(0).toUpperCase()}
                          </span>
                          <span>
                            <strong>{leave.employeeName || "Employee"}</strong>
                            <span className="enterprise-subtext">
                              Leave #{leave.leaveId}
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
                          {leave.approvalStatus === "PENDING_TEAM_LEAD" && (
                            <button
                              type="button"
                              className="btn btn-outline-success btn-sm"
                              onClick={() => handleTeamLeadApprove(leave.leaveId)}
                              disabled={actionLoadingId === leave.leaveId}
                            >
                              Team Lead
                            </button>
                          )}
                          {leave.approvalStatus === "PENDING_MANAGER" && (
                            <button
                              type="button"
                              className="btn btn-outline-success btn-sm"
                              onClick={() => handleManagerApprove(leave.leaveId)}
                              disabled={actionLoadingId === leave.leaveId}
                            >
                              Manager
                            </button>
                          )}
                          {leave.approvalStatus === "PENDING_HR" && (
                            <button
                              type="button"
                              className="btn btn-outline-success btn-sm"
                              onClick={() => handleHrApprove(leave.leaveId)}
                              disabled={actionLoadingId === leave.leaveId}
                            >
                              HR
                            </button>
                          )}
                          {leave.approvalStatus === "HR_REVIEWED" && (
                            <button
                              type="button"
                              className="btn btn-outline-primary btn-sm"
                              onClick={() => handleFinalApprove(leave.leaveId)}
                              disabled={actionLoadingId === leave.leaveId}
                            >
                              Final
                            </button>
                          )}
                          {leave.approvalStatus === "APPROVED" && (
                            <button
                              type="button"
                              className="btn btn-outline-warning btn-sm"
                              onClick={() => handleCancelLeave(leave.leaveId)}
                              disabled={actionLoadingId === leave.leaveId}
                            >
                              Cancel
                            </button>
                          )}
                          {!terminalStatuses.includes(leave.approvalStatus) && (
                            <button
                              type="button"
                              className="btn btn-outline-danger btn-sm"
                              onClick={() =>
                                setRejectModal({ open: true, leaveId: leave.leaveId })
                              }
                              disabled={actionLoadingId === leave.leaveId}
                            >
                              <FiSlash /> Reject
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="9">
                      <EmptyState
                        title="No matching leave requests"
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

      {chartItems.length > 0 && (
        <section className="enterprise-panel">
          <div className="enterprise-panel-header">
            <div>
              <h3>Approval Status Distribution</h3>
              <p>Current workflow shape based on loaded leave records.</p>
            </div>
          </div>
          <MiniBarChart items={chartItems} valueLabel="requests" />
        </section>
      )}

      {rejectModal.open && (
        <div className="enterprise-modal-overlay" role="presentation">
          <div className="enterprise-modal" role="dialog" aria-modal="true">
            <div className="enterprise-modal-header">
              <h3>Reject Leave</h3>
              <button
                type="button"
                className="enterprise-close"
                onClick={() => setRejectModal({ open: false, leaveId: null })}
                aria-label="Close reject dialog"
              >
                x
              </button>
            </div>
            <div className="enterprise-modal-body">
              <label className="enterprise-form-field">
                <span>Rejection reason</span>
                <textarea
                  className="form-control"
                  rows="4"
                  value={rejectReason}
                  onChange={(event) => setRejectReason(event.target.value)}
                  placeholder="Explain why this leave request is being rejected"
                />
              </label>
            </div>
            <div className="enterprise-modal-footer">
              <button
                type="button"
                className="btn btn-outline-secondary"
                onClick={() => setRejectModal({ open: false, leaveId: null })}
              >
                Close
              </button>
              <button
                type="button"
                className="btn btn-danger"
                onClick={handleRejectLeave}
                disabled={actionLoadingId === rejectModal.leaveId}
              >
                Reject Leave
              </button>
            </div>
          </div>
        </div>
      )}
    </EnterprisePage>
  );
}

export default LeaveList;
