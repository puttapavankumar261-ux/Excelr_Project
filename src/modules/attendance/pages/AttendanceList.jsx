import { useEffect, useMemo, useState } from "react";
import {
  FiCalendar,
  FiCheckCircle,
  FiClock,
  FiRefreshCw,
  FiTrendingUp,
  FiUsers,
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
import { getAllAttendance } from "../services/attendanceService";

const getStatus = (record) => record.attendanceStatus || record.status || "UNKNOWN";
const getDate = (record) => record.attendanceDate || record.date || "";
const getName = (record) => record.employeeName || record.employeename || "Employee";

function AttendanceList() {
  const [attendance, setAttendance] = useState([]);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [dateFilter, setDateFilter] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadAttendance = async () => {
    try {
      setLoading(true);
      setError("");
      const response = await getAllAttendance();
      setAttendance(response.data || []);
    } catch (loadError) {
      console.error("Error loading attendance", loadError);
      setError("Unable to load attendance records. Please retry or check the backend.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAttendance();
  }, []);

  const statusCounts = useMemo(() => {
    return attendance.reduce((acc, record) => {
      const status = getStatus(record);
      acc[status] = (acc[status] || 0) + 1;
      return acc;
    }, {});
  }, [attendance]);

  const filteredAttendance = useMemo(() => {
    const query = search.trim().toLowerCase();

    return attendance.filter((record) => {
      const searchable = [
        getName(record),
        record.employeeId,
        record.department,
        getStatus(record),
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      const employeeMatch = !query || searchable.includes(query);
      const statusMatch = statusFilter === "ALL" || getStatus(record) === statusFilter;
      const dateMatch = !dateFilter || getDate(record) === dateFilter;

      return employeeMatch && statusMatch && dateMatch;
    });
  }, [attendance, dateFilter, search, statusFilter]);

  const presentCount = statusCounts.PRESENT || 0;
  const absentCount = statusCounts.ABSENT || 0;
  const leaveCount = statusCounts.LEAVE || 0;
  const attendanceRate = attendance.length
    ? Math.round((presentCount / attendance.length) * 100)
    : 0;

  const chartItems = Object.entries(statusCounts)
    .map(([label, value]) => ({ label, value }))
    .sort((a, b) => b.value - a.value);

  return (
    <EnterprisePage>
      <PageHero
        eyebrow="Attendance Management"
        title="Attendance Operations"
        description="Track daily attendance, monitor exceptions, and filter records by employee, date, and status."
        icon={FiClock}
        meta={
          <>
            <span>{filteredAttendance.length} visible records</span>
            <span>{attendanceRate}% present rate</span>
          </>
        }
        actions={
          <button
            type="button"
            className="btn btn-light"
            onClick={loadAttendance}
            disabled={loading}
          >
            <FiRefreshCw /> Refresh
          </button>
        }
      />

      <ErrorBanner message={error} onRetry={loadAttendance} />

      <section className="enterprise-grid">
        <MetricCard
          label="Total Records"
          value={attendance.length}
          helper="Loaded attendance entries"
          icon={FiUsers}
          tone="blue"
        />
        <MetricCard
          label="Present"
          value={presentCount}
          helper={`${attendanceRate}% of records`}
          icon={FiCheckCircle}
          tone="green"
        />
        <MetricCard
          label="Absent"
          value={absentCount}
          helper="Needs attention"
          icon={FiXCircle}
          tone="red"
        />
        <MetricCard
          label="Leave"
          value={leaveCount}
          helper="Approved or marked leaves"
          icon={FiCalendar}
          tone="gold"
        />
      </section>

      <section className="enterprise-panel">
        <div className="enterprise-panel-header">
          <div>
            <h2>Attendance Register</h2>
            <p>Use filters together to isolate exceptions or verify a date.</p>
          </div>
        </div>

        <div className="enterprise-toolbar">
          <SearchField
            value={search}
            onChange={setSearch}
            placeholder="Search employee, department, status..."
          />

          <input
            type="date"
            className="form-control"
            value={dateFilter}
            onChange={(event) => setDateFilter(event.target.value)}
            aria-label="Filter attendance by date"
          />

          <select
            className="form-select"
            value={statusFilter}
            onChange={(event) => setStatusFilter(event.target.value)}
            aria-label="Filter attendance by status"
          >
            <option value="ALL">All Statuses</option>
            <option value="PRESENT">Present</option>
            <option value="ABSENT">Absent</option>
            <option value="LEAVE">Leave</option>
            <option value="HALF_DAY">Half Day</option>
            <option value="WEEK_OFF">Week Off</option>
            <option value="HOLIDAY">Holiday</option>
          </select>
        </div>

        {loading ? (
          <LoadingState label="Loading attendance..." />
        ) : attendance.length === 0 ? (
          <EmptyState
            title="No attendance records"
            message="Attendance records will appear here after check-ins or imports."
          />
        ) : (
          <div className="enterprise-table-wrap">
            <table className="enterprise-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Employee</th>
                  <th>Department</th>
                  <th>Date</th>
                  <th>Status</th>
                  <th>Punch In</th>
                  <th>Punch Out</th>
                </tr>
              </thead>
              <tbody>
                {filteredAttendance.length > 0 ? (
                  filteredAttendance.map((record) => (
                    <tr key={record.attendanceId || `${record.employeeId}-${getDate(record)}`}>
                      <td>{record.employeeId || "-"}</td>
                      <td>
                        <span className="enterprise-row-title">
                          <span className="enterprise-avatar">
                            {getName(record).charAt(0).toUpperCase()}
                          </span>
                          <span>
                            <strong>{getName(record)}</strong>
                            <span className="enterprise-subtext">
                              {record.employeeCode || "Employee record"}
                            </span>
                          </span>
                        </span>
                      </td>
                      <td>{record.department || "-"}</td>
                      <td>{formatDate(getDate(record))}</td>
                      <td>
                        <StatusBadge status={getStatus(record)} />
                      </td>
                      <td>{record.punchInTime || "-"}</td>
                      <td>{record.punchOutTime || "-"}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="7">
                      <EmptyState
                        title="No matching attendance"
                        message="Try adjusting the search, date, or status filter."
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
              <h3>Status Distribution</h3>
              <p>Live view of attendance statuses across loaded records.</p>
            </div>
            <FiTrendingUp />
          </div>
          <MiniBarChart items={chartItems} valueLabel="records" />
        </section>
      )}
    </EnterprisePage>
  );
}

export default AttendanceList;
