import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { setupAdmin } from "../services/setupService";
import { loginWithFallback, normalizeLoggedInUser } from "../services/authService";
import { getApiErrorMessage } from "../../../api/errorUtils";

function AdminSetup({ onClose }) {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    employeeName: "",
    username: "",
    password: "",
    confirmPassword: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSetup = async () => {
    try {
      setError("");
      setSuccess("");

      if (
        !formData.employeeName ||
        !formData.username ||
        !formData.password ||
        !formData.confirmPassword
      ) {
        setError("All fields are required");
        return;
      }

      if (formData.password !== formData.confirmPassword) {
        setError("Passwords do not match");
        return;
      }

      setLoading(true);

      const employeeName = formData.employeeName.trim();
      const username = formData.username.trim();
      const password = formData.password;

      // Create the admin employee and login in one backend transaction.
      await setupAdmin({
        employeeName,
        username,
        password,
        role: "ADMIN",
        designation: "MANAGER",
        department: "ADMIN",
      });

      const loginResponse = await loginWithFallback({
        username,
        password,
      });

      localStorage.setItem(
        "user",
        JSON.stringify(normalizeLoggedInUser(loginResponse.data)),
      );

      setSuccess("Admin account created successfully");

      setTimeout(() => {
        if (onClose) {
          onClose();
        }

        navigate("/admin");
      }, 1000);
    } catch (err) {
      console.error(err);

      setError(getApiErrorMessage(err, "Admin setup failed"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="admin-setup-card">
      <div className="card shadow p-4">
        <h2 className="text-center mb-4">System Admin Setup</h2>

        <input
          type="text"
          className="form-control mb-3"
          placeholder="Employee Name"
          name="employeeName"
          value={formData.employeeName}
          onChange={handleChange}
        />

        <input
          type="text"
          className="form-control mb-3"
          placeholder="Username"
          name="username"
          value={formData.username}
          onChange={handleChange}
        />

        <input
          type="password"
          className="form-control mb-3"
          placeholder="Password"
          name="password"
          value={formData.password}
          onChange={handleChange}
        />

        <input
          type="password"
          className="form-control mb-3"
          placeholder="Confirm Password"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
        />

        <button
          className="btn btn-success w-100"
          onClick={handleSetup}
          disabled={loading}
        >
          {loading ? "Creating Admin..." : "Create Admin"}
        </button>

        <button
          className="btn btn-outline-secondary w-100 mt-2"
          onClick={() => {
            if (onClose) {
              onClose();
            }
          }}
        >
          Cancel
        </button>

        {error && <div className="alert alert-danger mt-3">{error}</div>}

        {success && <div className="alert alert-success mt-3">{success}</div>}
      </div>
    </div>
  );
}

export default AdminSetup;
