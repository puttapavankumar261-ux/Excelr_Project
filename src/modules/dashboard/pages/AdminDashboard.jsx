import { useNavigate } from "react-router-dom";
import { useCallback, useEffect, useMemo, useState } from "react";
import {
  FiActivity,
  FiAlertCircle,
  FiArrowUpRight,
  FiBarChart2,
  FiBriefcase,
  FiCalendar,
  FiCheckCircle,
  FiClock,
  FiDollarSign,
  FiLogOut,
  FiSettings,
  FiUserPlus,
  FiUsers,
} from "react-icons/fi";

import { getEmployees } from "../../employee/services/employeeService";
import { getAllAttendance } from "../../attendance/services/attendanceService";

import "../styles/AdminDashboard.css";

const getInitialAdminProfile = () => {
  const storedProfile = JSON.parse(localStorage.getItem("adminProfile") || "null");
  const loggedInUser = JSON.parse(localStorage.getItem("user") || "null");

  if (storedProfile) {
    return storedProfile;
  }

  if (loggedInUser) {
    return {
      name: loggedInUser.username || "Admin User",
      email: loggedInUser.email || "admin@ems.com",
      phone: loggedInUser.phone || "+91 98765 43210",
      role: loggedInUser.role || "System Administrator",
    };
  }

  return {
    name: "Admin User",
    email: "admin@ems.com",
    phone: "+91 98765 43210",
    role: "System Administrator",
  };
};

function AdminDashboard() {
  const navigate = useNavigate();

  const [employees, setEmployees] = useState([]);
  const [attendance, setAttendance] = useState([]);
  const [loading, setLoading] = useState(true);
  const [dashboardError, setDashboardError] = useState("");
  const [profile] = useState(getInitialAdminProfile);

  const loadDashboardData = useCallback(async () => {
    try {
      setLoading(true);
      setDashboardError("");

      const [employeeResponse, attendanceResponse] = await Promise.all([
        getEmployees(),
        getAllAttendance(),
      ]);

      setEmployees(employeeResponse.data || []);
      setAttendance(attendanceResponse.data || []);
    } catch (error) {
      console.error("Error loading dashboard data:", error);
      setDashboardError("Dashboard data could not be refreshed. Showing the last available view.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadDashboardData();
  }, [loadDashboardData]);

  const departmentSummary = useMemo(() => {
    return Object.entries(
      employees.reduce((acc, emp) => {
        const department = emp.department || "Unknown";
        acc[department] = (acc[department] || 0) + 1;
        return acc;
      }, {}),
    ).sort((a, b) => b[1] - a[1]);
  }, [employees]);

  const presentToday = attendance.filter(
    (item) => item.attendanceStatus === "PRESENT" || item.status === "PRESENT",
  ).length;

  const attendanceRate = attendance.length
    ? Math.round((presentToday / attendance.length) * 100)
    : 0;

  const absentToday = Math.max(employees.length - presentToday, 0);
  const activeDepartments = departmentSummary.length;
  const recentEmployees = employees.slice(0, 5);
  const topDepartments = departmentSummary.slice(0, 4);
  const adminInitial = (profile.name || "A").charAt(0).toUpperCase();
  const todayLabel = new Intl.DateTimeFormat("en-IN", {
    weekday: "short",
    day: "2-digit",
    month: "short",
  }).format(new Date());

  const stats = [
    {
      label: "Total Employees",
      value: employees.length,
      helper: "Active workforce records",
      icon: FiUsers,
      tone: "tone-blue",
      path: "/admin/employees",
    },
    {
      label: "Present Today",
      value: presentToday,
      helper: "Marked present in attendance",
      icon: FiCheckCircle,
      tone: "tone-green",
      path: "/admin/attendance",
    },
    {
      label: "Departments",
      value: activeDepartments,
      helper: "Teams currently tracked",
      icon: FiBriefcase,
      tone: "tone-gold",
      path: "/admin/employees",
    },
    {
      label: "Attendance Rate",
      value: `${attendanceRate}%`,
      helper: "Based on loaded records",
      icon: FiActivity,
      tone: "tone-red",
      path: "/admin/attendance-summary",
    },
  ];

  const quickActions = [
    {
      label: "Add Employee",
      icon: FiUserPlus,
      tone: "action-blue",
      path: "/admin/employees/add",
    },
    {
      label: "Generate Payroll",
      icon: FiDollarSign,
      tone: "action-green",
      path: "/admin/payroll",
    },
    {
      label: "Approve Leaves",
      icon: FiCalendar,
      tone: "action-gold",
      path: "/admin/leave-approval",
    },
    {
      label: "View Reports",
      icon: FiBarChart2,
      tone: "action-dark",
      path: "/admin/reports",
    },
  ];

  const systemPulse = [
    {
      label: "Attendance",
      value: `${attendanceRate}%`,
      detail: `${presentToday} present, ${absentToday} pending`,
    },
    {
      label: "Workforce",
      value: employees.length,
      detail: `${activeDepartments} active departments`,
    },
    {
      label: "Data Status",
      value: loading ? "Syncing" : "Ready",
      detail: dashboardError || "Dashboard data is up to date",
    },
  ];

  const handleLogout = () => {
    localStorage.removeItem("user");
    sessionStorage.removeItem("user");
    navigate("/login");
  };

  const getEmployeeName = (employee) => {
    return employee.employeeName || employee.employeename || "Unnamed Employee";
  };

  return (
    <div className="dashboard-container">
      <section className="dashboard-hero admin-hero">
        <div>
          <span className="dashboard-eyebrow">Admin Command Center</span>
          <h1>Welcome, {profile.name}</h1>
          <p>
            Review workforce health, track attendance, and jump into the core HR
            workflows from one organized dashboard.
          </p>
          <div className="hero-chip-row">
            <span>{todayLabel}</span>
            <span>{loading ? "Refreshing data" : "Live workspace"}</span>
          </div>
        </div>

        <div className="hero-profile-panel">
          <span className="profile-avatar">{adminInitial}</span>
          <div>
            <strong>{profile.role}</strong>
            <span>{profile.email}</span>
          </div>
        </div>
      </section>

      {(dashboardError || loading) && (
        <div className={`dashboard-notice ${dashboardError ? "is-warning" : ""}`}>
          <FiAlertCircle />
          <span>{dashboardError || "Refreshing dashboard data..."}</span>
        </div>
      )}

      <section className="pulse-grid">
        {systemPulse.map((item) => (
          <div className="pulse-card" key={item.label}>
            <span>{item.label}</span>
            <strong>{item.value}</strong>
            <small>{item.detail}</small>
          </div>
        ))}
      </section>

      <section className="stats-grid">
        {stats.map((stat) => {
          const Icon = stat.icon;

          return (
            <button
              type="button"
              className={`stat-card ${stat.tone}`}
              key={stat.label}
              onClick={() => navigate(stat.path)}
            >
              <span className="stat-icon">
                <Icon />
              </span>
              <span className="stat-copy">
                <span>{stat.label}</span>
                <strong>{stat.value}</strong>
                <small>{stat.helper}</small>
              </span>
              <FiArrowUpRight className="stat-arrow" />
            </button>
          );
        })}
      </section>

      <section className="dashboard-row dashboard-row-large">
        <div className="dashboard-card">
          <div className="card-heading">
            <div>
              <span className="section-label">Profile</span>
              <h3>Admin Profile</h3>
            </div>
            <FiSettings />
          </div>

          <div className="detail-list">
            <p>
              <strong>Name</strong>
              <span>{profile.name}</span>
            </p>
            <p>
              <strong>Email</strong>
              <span>{profile.email}</span>
            </p>
            <p>
              <strong>Phone</strong>
              <span>{profile.phone}</span>
            </p>
            <p>
              <strong>Role</strong>
              <span>{profile.role}</span>
            </p>
          </div>

          <div className="action-buttons mt-3">
            <button className="btn btn-primary" onClick={() => navigate("/admin/settings")}>
              <FiSettings /> Edit Profile
            </button>
            <button className="btn btn-outline-danger" onClick={handleLogout}>
              <FiLogOut /> Logout
            </button>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-heading">
            <div>
              <span className="section-label">Operations</span>
              <h3>Quick Actions</h3>
            </div>
            <FiClock />
          </div>

          <div className="quick-action-grid">
            {quickActions.map((action) => {
              const Icon = action.icon;

              return (
                <button
                  type="button"
                  key={action.label}
                  className={`quick-action-card ${action.tone}`}
                  onClick={() => navigate(action.path)}
                >
                  <span>
                    <Icon />
                  </span>
                  <strong>{action.label}</strong>
                  <FiArrowUpRight />
                </button>
              );
            })}
          </div>
        </div>
      </section>

      <section className="dashboard-row">
        <div className="dashboard-card">
          <div className="card-heading">
            <div>
              <span className="section-label">Departments</span>
              <h3>Department Summary</h3>
            </div>
            <FiBriefcase />
          </div>

          <div className="department-bars">
            {topDepartments.length > 0 ? (
              topDepartments.map(([department, count]) => {
                const width = employees.length
                  ? Math.max(8, Math.round((count / employees.length) * 100))
                  : 0;

                return (
                  <div className="department-bar" key={department}>
                    <div>
                      <strong>{department}</strong>
                      <span>{count} employees</span>
                    </div>
                    <div className="department-track">
                      <span style={{ width: `${width}%` }} />
                    </div>
                  </div>
                );
              })
            ) : (
              <div className="empty-state">No department data available</div>
            )}
          </div>

          <div className="table-responsive">
            <table className="table dashboard-table">
              <thead>
                <tr>
                  <th>Department</th>
                  <th>Employees</th>
                </tr>
              </thead>

              <tbody>
                {departmentSummary.length > 0 ? (
                  departmentSummary.map(([department, count]) => (
                    <tr key={department}>
                      <td>{department}</td>
                      <td>
                        <span className="table-pill">{count}</span>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="2">No department data available</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-heading">
            <div>
              <span className="section-label">Highlights</span>
              <h3>Today at a Glance</h3>
            </div>
            <FiActivity />
          </div>

          <div className="insight-list">
            <div>
              <strong>{employees.length}</strong>
              <span>employees currently tracked</span>
            </div>
            <div>
              <strong>{presentToday}</strong>
              <span>employees marked present</span>
            </div>
            <div>
              <strong>{activeDepartments}</strong>
              <span>departments active in the system</span>
            </div>
            <div>
              <strong>{attendanceRate}%</strong>
              <span>attendance completion rate</span>
            </div>
          </div>
        </div>
      </section>

      <section className="dashboard-card">
        <div className="card-heading">
          <div>
            <span className="section-label">People</span>
            <h3>Recent Employees</h3>
          </div>
          <button
            type="button"
            className="btn btn-outline-primary btn-sm"
            onClick={() => navigate("/admin/employees")}
          >
            View All
          </button>
        </div>

        <div className="table-responsive">
          <table className="table dashboard-table employee-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Department</th>
                <th>Designation</th>
              </tr>
            </thead>

            <tbody>
              {recentEmployees.length > 0 ? (
                recentEmployees.map((employee) => (
                  <tr
                    key={employee.employeeid}
                    onClick={() => navigate("/admin/employees")}
                  >
                    <td>{employee.employeeid}</td>
                    <td>
                      <span className="employee-cell">
                        <span className="employee-avatar">
                          {getEmployeeName(employee).charAt(0).toUpperCase()}
                        </span>
                        {getEmployeeName(employee)}
                      </span>
                    </td>
                    <td>{employee.department || "-"}</td>
                    <td>
                      <span className="role-pill">{employee.designation || "-"}</span>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4">No employees found</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}

export default AdminDashboard;
