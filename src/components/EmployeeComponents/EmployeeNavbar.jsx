import { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { FiLogOut, FiMenu } from "react-icons/fi";
import "../../styles/EmployeeNavbar.css";

function EmployeeNavbar() {
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const closeMenu = () => setMenuOpen(false);

  const handleLogout = () => {
    localStorage.removeItem("user");
    sessionStorage.removeItem("user");
    navigate("/login");
  };

  return (
    <nav className="employee-navbar">
      <div className="employee-navbar-brand">
        <img src="/ems-logo.png" alt="EMS" />
        <div>
          <strong>Employee Portal</strong>
          <span>Personal workspace</span>
        </div>
      </div>

      <button
        type="button"
        className="employee-menu-toggle"
        onClick={() => setMenuOpen((prev) => !prev)}
        aria-label="Toggle employee navigation"
      >
        <FiMenu />
      </button>

      <div className={`employee-nav-links ${menuOpen ? "open" : ""}`}>
        <NavLink to="/employee/dashboard" onClick={closeMenu}>
          Dashboard
        </NavLink>

        <NavLink to="/employee/profile" onClick={closeMenu}>
          Profile
        </NavLink>

        <NavLink to="/employee/attendance" onClick={closeMenu}>
          Attendance
        </NavLink>

        <NavLink to="/employee/leave" onClick={closeMenu}>
          Leave
        </NavLink>

        <NavLink to="/employee/payslips" onClick={closeMenu}>
          Payslips
        </NavLink>

        <NavLink to="/employee/settings" onClick={closeMenu}>
          Settings
        </NavLink>

        <button className="employee-logout-btn" onClick={handleLogout}>
          <FiLogOut /> Logout
        </button>
      </div>
    </nav>
  );
}

export default EmployeeNavbar;
