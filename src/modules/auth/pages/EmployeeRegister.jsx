import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { saveEmployee, saveLogin } from "../services/setupService";
import "../styles/Login.css";

function EmployeeRegister() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    employeeName: "",
    email: "",
    department: "General",
    designation: "Employee",
    username: "",
    password: "",
    confirmPassword: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleRegister = async () => {
    try {
      setError("");
      setSuccess("");

      if (
        !formData.employeeName ||
        !formData.email ||
        !formData.username ||
        !formData.password ||
        !formData.confirmPassword
      ) {
        setError("Please fill in all required fields.");
        return;
      }

      if (formData.password !== formData.confirmPassword) {
        setError("Passwords do not match.");
        return;
      }

      setLoading(true);

      const employeeResponse = await saveEmployee({
        employeeName: formData.employeeName,
        role: "EMPLOYEE",
        designation: formData.designation || "Employee",
        department: formData.department || "General",
        employmentStatus: "ACTIVE",
        email: formData.email,
      });

      const employeeId = employeeResponse?.data?.employeeid;

      if (!employeeId) {
        throw new Error("Employee ID was not returned from the server.");
      }

      await saveLogin({
        employee: { employeeid: employeeId },
        username: formData.username,
        passwordHash: formData.password,
        role: "EMPLOYEE",
        status: "ACTIVE",
      });

      setSuccess("Employee account created successfully. You can now login.");

      setTimeout(() => navigate("/login"), 1000);
    } catch (err) {
      console.error(err);

      if (err.code === "ECONNREFUSED" || err.message?.includes("ECONNREFUSED")) {
        setError("Backend server is not running. Please start the backend first.");
      } else if (err.response) {
        setError(err.response.data.message || "Employee registration failed.");
      } else {
        setError(err.message || "Employee registration failed.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="hero-section">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-lg-6 col-md-8">
            <div className="login-card">
              <h2 className="text-center mb-3">Employee Registration</h2>
              <p className="text-muted text-center mb-4">Create a basic employee account with login access.</p>

              <input className="form-control mb-3" name="employeeName" placeholder="Full Name" value={formData.employeeName} onChange={handleChange} />
              <input className="form-control mb-3" name="email" placeholder="Email" value={formData.email} onChange={handleChange} />
              <input className="form-control mb-3" name="department" placeholder="Department" value={formData.department} onChange={handleChange} />
              <input className="form-control mb-3" name="designation" placeholder="Designation" value={formData.designation} onChange={handleChange} />
              <input className="form-control mb-3" name="username" placeholder="Username" value={formData.username} onChange={handleChange} />
              <input className="form-control mb-3" type="password" name="password" placeholder="Password" value={formData.password} onChange={handleChange} />
              <input className="form-control mb-3" type="password" name="confirmPassword" placeholder="Confirm Password" value={formData.confirmPassword} onChange={handleChange} />

              {error && <div className="alert alert-danger">{error}</div>}
              {success && <div className="alert alert-success">{success}</div>}

              <button className="btn btn-primary w-100" onClick={handleRegister} disabled={loading}>
                {loading ? "Creating Account..." : "Create Employee Account"}
              </button>

              <button className="btn btn-outline-secondary w-100 mt-2" onClick={() => navigate("/login")}>
                Back to Login
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

export default EmployeeRegister;
