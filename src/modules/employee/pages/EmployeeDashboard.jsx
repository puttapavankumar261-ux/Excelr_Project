import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FiCalendar,
  FiClock,
  FiCreditCard,
  FiLock,
  FiLogOut,
  FiMapPin,
  FiUser,
  FiUsers,
} from "react-icons/fi";

import { getEmployeeById } from "../services/employeeService";
import { getAttendanceSummary } from "../../attendance/services/attendanceService";
import "../../dashboard/styles/AdminDashboard.css";

function EmployeeDashboard() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [employee, setEmployee] = useState(null);
  const [attendance, setAttendance] = useState(null);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const storedUser = JSON.parse(localStorage.getItem("user") || "null");
        const employeeId =
          storedUser?.id || storedUser?.employeeid || storedUser?.employee?.employeeid;

        if (!employeeId) {
          navigate("/login");
          return;
        }

        setUser({ ...storedUser, id: employeeId });

        const employeeResponse = await getEmployeeById(employeeId);
        setEmployee(employeeResponse.data);

        const attendanceResponse = await getAttendanceSummary(employeeId);
        setAttendance(attendanceResponse.data);
      } catch (error) {
        console.error(error);
      }
    };

    loadDashboard();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("user");
    sessionStorage.removeItem("user");
    navigate("/login");
  };

  const employeeName =
    employee?.employeename ||
    employee?.employeeName ||
    user?.username ||
    "Employee";

  const attendancePercentage =
    attendance?.attendancePercentage ||
    (attendance?.workingDays
      ? Math.round(((attendance?.presentDays || 0) / attendance.workingDays) * 100)
      : 0);

  const stats = [
    {
      label: "Employee ID",
      value: employee?.employeeid || user?.id || "-",
      helper: "Your HRMS profile number",
      icon: FiUser,
    },
    {
      label: "Department",
      value: employee?.department || "-",
      helper: "Team assignment",
      icon: FiUsers,
    },
    {
      label: "Designation",
      value: employee?.designation || "-",
      helper: "Current position",
      icon: FiCreditCard,
    },
    {
      label: "Status",
      value: employee?.employmentStatus || employee?.role || "-",
      helper: "Employment record",
      icon: FiClock,
    },
  ];

  const actions = [
    {
      label: "My Profile",
      icon: FiUser,
      className: "btn btn-primary",
      path: "/employee/profile",
    },
    {
      label: "Attendance",
      icon: FiClock,
      className: "btn btn-success",
      path: "/employee/attendance",
    },
    {
      label: "Leave Requests",
      icon: FiCalendar,
      className: "btn btn-warning",
      path: "/employee/leave",
    },
    {
      label: "Payslips",
      icon: FiCreditCard,
      className: "btn btn-info",
      path: "/employee/payslips",
    },
    {
      label: "Change Password",
      icon: FiLock,
      className: "btn btn-outline-secondary",
      path: "/employee/change-password",
    },
  ];

  return (
    <div className="dashboard-container">
      <section className="dashboard-hero employee-hero">
        <div>
          <span className="dashboard-eyebrow">Employee Portal</span>
          <h1>Welcome, {employeeName}</h1>
          <p>
            Keep track of your profile, attendance, leaves, payslips and account
            settings from a single personal workspace.
          </p>
        </div>

        <div className="hero-profile-panel">
          <span className="profile-avatar">{employeeName.charAt(0).toUpperCase()}</span>
          <div>
            <strong>{employee?.designation || "Employee"}</strong>
            <span>{employee?.department || "Department not assigned"}</span>
          </div>
        </div>
      </section>

      <section className="stats-grid">
        {stats.map((stat) => {
          const Icon = stat.icon;

          return (
            <div className="stat-card" key={stat.label}>
              <span className="stat-icon">
                <Icon />
              </span>
              <span className="stat-copy">
                <span>{stat.label}</span>
                <strong>{stat.value}</strong>
                <small>{stat.helper}</small>
              </span>
            </div>
          );
        })}
      </section>

      <section className="dashboard-row dashboard-row-large">
        <div className="dashboard-card">
          <div className="card-heading">
            <div>
              <span className="section-label">Attendance</span>
              <h3>Monthly Summary</h3>
            </div>
            <FiClock />
          </div>

          <div className="insight-list">
            <div>
              <strong>{attendance?.workingDays || 0}</strong>
              <span>working days</span>
            </div>
            <div>
              <strong>{attendance?.presentDays || 0}</strong>
              <span>present days</span>
            </div>
            <div>
              <strong>{attendance?.leaveDays || 0}</strong>
              <span>leave days</span>
            </div>
            <div>
              <strong>{attendance?.absentDays || 0}</strong>
              <span>absent days</span>
            </div>
          </div>

          <div className="progress-metric">
            <div className="progress-metric-header">
              <span>Attendance Percentage</span>
              <strong>{attendancePercentage}%</strong>
            </div>
            <div className="progress-track">
              <div
                className="progress-fill"
                style={{ width: `${Math.min(attendancePercentage, 100)}%` }}
              />
            </div>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-heading">
            <div>
              <span className="section-label">Profile</span>
              <h3>Employee Information</h3>
            </div>
            <FiUser />
          </div>

          <div className="detail-list">
            <p>
              <strong>Name</strong>
              <span>{employeeName}</span>
            </p>
            <p>
              <strong>Department</strong>
              <span>{employee?.department || "-"}</span>
            </p>
            <p>
              <strong>Designation</strong>
              <span>{employee?.designation || "-"}</span>
            </p>
            <p>
              <strong>Status</strong>
              <span>{employee?.employmentStatus || "-"}</span>
            </p>
            <p>
              <strong>Work Location</strong>
              <span>
                <FiMapPin /> {employee?.workLocation || "-"}
              </span>
            </p>
          </div>
        </div>
      </section>

      <section className="dashboard-card">
        <div className="card-heading">
          <div>
            <span className="section-label">Shortcuts</span>
            <h3>Quick Actions</h3>
          </div>
          <button className="btn btn-outline-danger btn-sm" onClick={handleLogout}>
            <FiLogOut /> Logout
          </button>
        </div>

        <div className="employee-action-panel">
          {actions.map((action) => {
            const Icon = action.icon;

            return (
              <button
                type="button"
                className={action.className}
                key={action.label}
                onClick={() => navigate(action.path)}
              >
                <Icon /> {action.label}
              </button>
            );
          })}
        </div>
      </section>
    </div>
  );
}

export default EmployeeDashboard;
