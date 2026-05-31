import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";

/* Images */
import logo from "../assets/ems-logo.png";
import employeeBot from "../assets/home-employeebot.png";

/* CSS */
import "../styles/home.css";

const Home = () => {
  const navigate = useNavigate();

  const [showLogin, setShowLogin] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    axios
      .get("http://localhost:8080/check-admin")
      .then((response) => {
        if (!response.data) {
          navigate("/register");
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }, [navigate]);

  const handleLogin = async () => {
    try {
      const response = await axios.post("http://localhost:8080/login", {
        username,
        password,
      });

      localStorage.setItem("user", JSON.stringify(response.data));

      const role = response.data.role;

      if (role === "ADMIN" || role === "HR" || role === "MANAGER") {
        navigate("/admin/dashboard");
      } else if (role === "EMPLOYEE") {
        navigate("/employee/dashboard");
      }
    } catch (err) {
      setError("Invalid Username or Password");
    }
  };

  return (
    <div className="home-page">
      {/* ================= NAVBAR ================= */}
      <nav className="navbar navbar-expand-lg custom-navbar">
        <div className="container-fluid">
          <Link className="navbar-brand d-flex align-items-center gap-3" to="/">
            <img src={logo} alt="EMS Logo" className="logo-img" />

            <div>
              <h4 className="logo-title">EMS Portal</h4>

              <small className="logo-subtitle">
                Employee Management System
              </small>
            </div>
          </Link>

          <button
            className="btn btn-primary px-4"
            onClick={() => setShowLogin(true)}
          >
            Login
          </button>
        </div>
      </nav>
      {/* ================= HERO ================= */}
      <div className="container hero-section">
        <div className="row align-items-center">
          <div className="col-lg-6">
            <span className="badge bg-primary mb-3">Smart HRMS Platform</span>

            <h1 className="hero-title">
              Transform Your Workforce With
              <span className="hero-highlight"> Modern HR Management</span>
            </h1>

            <p className="hero-description">
              Manage employees, payroll, attendance, departments, leave requests
              and HR operations through a secure and scalable platform.
            </p>
          </div>

          <div className="col-lg-6 text-center">
            <img
              src={employeeBot}
              alt="Employee Bot"
              className="hero-image img-fluid"
            />
          </div>
        </div>
      </div>
      {/* ================= FEATURES ================= */}
      <section className="container py-5">
        <div className="text-center mb-5">
          <h2 className="fw-bold">Powerful HRMS Features</h2>

          <p className="text-muted">
            Everything you need to manage employees efficiently
          </p>
        </div>

        <div className="row g-4">
          <div className="col-md-4">
            <div className="card border-0 shadow feature-card">
              <div className="card-body text-center">
                <h1>👨‍💼</h1>
                <h4>Employee Management</h4>
                <p>Add, edit and manage employee information with ease.</p>
              </div>
            </div>
          </div>

          <div className="col-md-4">
            <div className="card border-0 shadow feature-card">
              <div className="card-body text-center">
                <h1>⏰</h1>
                <h4>Attendance Tracking</h4>
                <p>Track attendance, leaves and work hours.</p>
              </div>
            </div>
          </div>

          <div className="col-md-4">
            <div className="card border-0 shadow feature-card">
              <div className="card-body text-center">
                <h1>💰</h1>
                <h4>Payroll Management</h4>
                <p>Automate salary calculations and payroll reports.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ================= STATS ================= */}
      <section className="stats-section">
        <div className="container">
          <div className="row text-center">
            <div className="col-md-3">
              <h2>500+</h2>
              <p>Employees</p>
            </div>

            <div className="col-md-3">
              <h2>50+</h2>
              <p>Departments</p>
            </div>

            <div className="col-md-3">
              <h2>98%</h2>
              <p>Attendance Accuracy</p>
            </div>

            <div className="col-md-3">
              <h2>24/7</h2>
              <p>Availability</p>
            </div>
          </div>
        </div>
      </section>

      {/* ================= WHY CHOOSE US ================= */}
      <section className="container py-5">
        <div className="row align-items-center">
          <div className="col-lg-6">
            <img
              src={employeeBot}
              alt="EMS"
              className="img-fluid rounded shadow"
            />
          </div>

          <div className="col-lg-6">
            <h2 className="fw-bold mb-4">Why Choose EMS Portal?</h2>

            <ul className="list-group list-group-flush">
              <li className="list-group-item">✅ Secure Employee Records</li>

              <li className="list-group-item">✅ Attendance Management</li>

              <li className="list-group-item">✅ Payroll Automation</li>

              <li className="list-group-item">✅ Role Based Access</li>
            </ul>
          </div>
        </div>
      </section>

      {/* ================= CTA ================= */}
      <section className="cta-section">
        <div className="container text-center">
          <h2>Ready To Simplify HR Operations?</h2>

          <p>
            Manage employees, attendance and payroll in one powerful platform.
          </p>

          <button
            className="btn btn-light btn-lg"
            onClick={() => setShowLogin(true)}
          >
            Get Started
          </button>
        </div>
      </section>

      {/* ================= FOOTER ================= */}
      <footer className="footer-section">
        <div className="container">
          <div className="row">
            <div className="col-md-6">
              <h5>EMS Portal</h5>

              <p>Smart Employee Management System for modern organizations.</p>
            </div>

            <div className="col-md-6 text-md-end">
              <p>© 2026 EMS Portal</p>

              <p>All Rights Reserved</p>
            </div>
          </div>
        </div>
      </footer>
      {/* ================= LOGIN MODAL ================= */}
      {showLogin && (
        <div className="login-overlay">
          <div className="login-modal">
            <button className="close-btn" onClick={() => setShowLogin(false)}>
              ✕
            </button>

            <div className="text-center mb-4">
              <img src={logo} alt="EMS Logo" width="90" />

              <h2 className="mt-3 fw-bold login-heading">Welcome Back</h2>

              <p className="text-muted">Sign in to EMS Portal</p>
            </div>

            <div className="mb-3">
              <label className="form-label fw-semibold">Username</label>

              <input
                type="text"
                className="form-control login-input"
                placeholder="Enter Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>

            <div className="mb-4">
              <label className="form-label fw-semibold">Password</label>

              <input
                type="password"
                className="form-control login-input"
                placeholder="Enter Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>

            <button
              className="btn btn-primary w-100 login-button"
              onClick={handleLogin}
            >
              Login
            </button>

            {error && <p className="text-danger text-center mt-3">{error}</p>}
          </div>
        </div>
      )}
    </div>
  );
};

export default Home;
