import { useEffect, useMemo, useState } from "react";
import {
  FiActivity,
  FiCalendar,
  FiCheckCircle,
  FiRefreshCw,
  FiTrendingUp,
  FiXCircle,
} from "react-icons/fi";

import {
  EnterprisePage,
  ErrorBanner,
  LoadingState,
  MetricCard,
  MiniBarChart,
  PageHero,
} from "../../../components/ui/EnterpriseUI";
import { getAllAttendance } from "../services/attendanceService";

const getStatus = (record) => record.attendanceStatus || record.status || "UNKNOWN";
const getDate = (record) => record.attendanceDate || record.date || "Unknown";

function AttendanceSummary() {
  const [attendance, setAttendance] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadData = async () => {
    try {
      setLoading(true);
      setError("");
      const response = await getAllAttendance();
      setAttendance(response.data || []);
    } catch (loadError) {
      console.error(loadError);
      setError("Unable to refresh attendance summary.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const statusSummary = useMemo(() => {
    return attendance.reduce((acc, record) => {
      const status = getStatus(record);
      acc[status] = (acc[status] || 0) + 1;
      return acc;
    }, {});
  }, [attendance]);

  const dailySummary = useMemo(() => {
    const counts = attendance.reduce((acc, record) => {
      const date = getDate(record);
      acc[date] = (acc[date] || 0) + 1;
      return acc;
    }, {});

    return Object.entries(counts)
      .map(([label, value]) => ({ label, value }))
      .sort((a, b) => String(b.label).localeCompare(String(a.label)))
      .slice(0, 7);
  }, [attendance]);

  const presentCount = statusSummary.PRESENT || 0;
  const absentCount = statusSummary.ABSENT || 0;
  const leaveCount = statusSummary.LEAVE || 0;
  const holidayCount = statusSummary.HOLIDAY || 0;
  const presentRate = attendance.length
    ? Math.round((presentCount / attendance.length) * 100)
    : 0;

  const statusChart = Object.entries(statusSummary)
    .map(([label, value]) => ({ label, value }))
    .sort((a, b) => b.value - a.value);

  return (
    <EnterprisePage>
      <PageHero
        eyebrow="Attendance Analytics"
        title="Attendance Summary"
        description="Review attendance trends, exceptions, and recent daily volume from the same live attendance dataset."
        icon={FiActivity}
        meta={
          <>
            <span>{attendance.length} records analyzed</span>
            <span>{presentRate}% present rate</span>
          </>
        }
        actions={
          <button
            type="button"
            className="btn btn-light"
            onClick={loadData}
            disabled={loading}
          >
            <FiRefreshCw /> Refresh
          </button>
        }
      />

      <ErrorBanner message={error} onRetry={loadData} />

      <section className="enterprise-grid">
        <MetricCard
          label="Total Records"
          value={attendance.length}
          helper="All loaded attendance entries"
          icon={FiActivity}
          tone="blue"
        />
        <MetricCard
          label="Present"
          value={presentCount}
          helper={`${presentRate}% present rate`}
          icon={FiCheckCircle}
          tone="green"
        />
        <MetricCard
          label="Absent"
          value={absentCount}
          helper="Exception count"
          icon={FiXCircle}
          tone="red"
        />
        <MetricCard
          label="Leave/Holiday"
          value={leaveCount + holidayCount}
          helper={`${leaveCount} leave, ${holidayCount} holiday`}
          icon={FiCalendar}
          tone="gold"
        />
      </section>

      {loading ? (
        <section className="enterprise-panel">
          <LoadingState label="Building attendance summary..." />
        </section>
      ) : (
        <section className="enterprise-grid">
          <div className="enterprise-panel">
            <div className="enterprise-panel-header">
              <div>
                <h3>Status Mix</h3>
                <p>Distribution by attendance status.</p>
              </div>
              <FiTrendingUp />
            </div>
            <MiniBarChart items={statusChart} valueLabel="records" />
          </div>

          <div className="enterprise-panel">
            <div className="enterprise-panel-header">
              <div>
                <h3>Recent Daily Volume</h3>
                <p>Latest dates with recorded attendance.</p>
              </div>
              <FiCalendar />
            </div>
            <MiniBarChart items={dailySummary} valueLabel="records" />
          </div>
        </section>
      )}
    </EnterprisePage>
  );
}

export default AttendanceSummary;
