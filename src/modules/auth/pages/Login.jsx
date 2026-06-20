import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FiActivity,
  FiArrowRight,
  FiBriefcase,
  FiCheckCircle,
  FiLock,
  FiShield,
  FiUser,
  FiUserPlus,
} from "react-icons/fi";
import AdminSetup from "./AdminSetup";

import {
  getLoginRole,
  loginWithFallback,
  normalizeLoggedInUser,
} from "../services/authService";
import { getApiErrorMessage } from "../../../api/errorUtils";

import "../styles/Login.css";

function Login() {
  const navigate = useNavigate();

  const [loginData, setLoginData] = useState({
    username: "",
    password: "",
  });

  const [showAdminSetup, setShowAdminSetup] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const features = [
    "Employee management",
    "Attendance tracking",
    "Payroll automation",
    "Recruitment portal",
    "Performance analytics",
    "Leave management",
  ];

  const platformStats = [
    { label: "Core Modules", value: "6" },
    { label: "Portal Access", value: "3" },
    { label: "Status", value: "Secure" },
  ];

  const handleChange = (e) => {
    setLoginData({
      ...loginData,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async () => {
    try {
      setError("");

      if (!loginData.username || !loginData.password) {
        setError("Username and Password are required");
        return;
      }

      setLoading(true);

      const response = await loginWithFallback({
        username: loginData.username.trim(),
        password: loginData.password,
      });

      console.log("Login Response:", response.data);

      localStorage.setItem(
        "user",
        JSON.stringify(normalizeLoggedInUser(response.data)),
      );

      const role = getLoginRole(response.data);

      switch (role) {
        case "ADMIN":
          navigate("/admin");
          break;

        case "EMPLOYEE":
          navigate("/employee");
          break;

        case "CANDIDATE":
          navigate("/candidate");
          break;

        default:
          setError("Invalid role received from server");
      }
    } catch (err) {
      console.error("Login Error:", err);

      setError(getApiErrorMessage(err, "Invalid username or password"));
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleLogin();
    }
  };

  return (
    <>
      <section className="hero-section">
        <div className="login-shell container">
          <div className="row align-items-center g-5">
            <div className="col-lg-7">
              <div className="hero-copy">
                <span className="hero-eyebrow">
                  <FiBriefcase /> HRMS Workspace
                </span>

                <h1 className="hero-title">Employee Management System</h1>

                <p className="hero-description">
                  Manage employees, attendance, payroll, recruitment and
                  workforce analytics from one focused platform.
                </p>

                <div className="login-stat-strip">
                  {platformStats.map((item) => (
                    <div key={item.label}>
                      <strong>{item.value}</strong>
                      <span>{item.label}</span>
                    </div>
                  ))}
                </div>

                <div className="feature-list">
                  {features.map((feature) => (
                    <div key={feature}>
                      <FiCheckCircle /> {feature}
                    </div>
                  ))}
                </div>
              </div>

              <img
                src="/home-employeebot.png"
                alt="EMS"
                className="hero-image"
              />
            </div>

            <div className="col-lg-5">
              <div className="login-card">
                <div className="text-center mb-4">
                  <img src="/ems-logo.png" alt="EMS" className="login-logo" />

                  <h2 className="mt-3">Welcome back</h2>

                  <p className="text-muted">
                    Sign in to continue to your portal.
                  </p>
                </div>

                <div className="login-status-pill">
                  <FiActivity /> Workspace authentication
                </div>

                <label className="input-label" htmlFor="username">
                  Username
                </label>
                <div className="input-with-icon mb-3">
                  <FiUser />
                  <input
                    id="username"
                    type="text"
                    name="username"
                    className="form-control"
                    placeholder="Enter username"
                    value={loginData.username}
                    onChange={handleChange}
                    onKeyDown={handleKeyPress}
                  />
                </div>

                <label className="input-label" htmlFor="password">
                  Password
                </label>
                <div className="input-with-icon mb-3">
                  <FiLock />
                  <input
                    id="password"
                    type="password"
                    name="password"
                    className="form-control"
                    placeholder="Enter password"
                    value={loginData.password}
                    onChange={handleChange}
                    onKeyDown={handleKeyPress}
                  />
                </div>

                {error && <div className="alert alert-danger">{error}</div>}

                <button
                  className="btn btn-primary login-primary-btn w-100"
                  onClick={handleLogin}
                  disabled={loading}
                >
                  {loading ? "Logging in..." : "Login"} <FiArrowRight />
                </button>

                <div className="account-actions mt-4">
                  <small className="text-muted">First time using EMS?</small>

                  <button
                    className="btn btn-outline-primary w-100"
                    onClick={() => setShowAdminSetup(true)}
                  >
                    <FiShield /> Create Admin Account
                  </button>

                  <button
                    className="btn btn-outline-success w-100"
                    onClick={() => navigate("/register")}
                  >
                    <FiUserPlus /> Create Employee Account
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {showAdminSetup && (
        <div className="admin-modal-overlay">
          <div
            className="admin-modal-content"
            onClick={(e) => e.stopPropagation()}
          >
            <AdminSetup
              onClose={() => {
                console.log("Closing Modal");
                setShowAdminSetup(false);
              }}
            />
          </div>
        </div>
      )}

      <footer className="footer-section">
        <div className="container text-center">
          Copyright 2026 Employee Management System | All Rights Reserved
        </div>
      </footer>
    </>
  );
}

export default Login;
