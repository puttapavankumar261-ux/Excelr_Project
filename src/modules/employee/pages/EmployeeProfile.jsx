import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FiBriefcase,
  FiKey,
  FiMail,
  FiMapPin,
  FiRefreshCw,
  FiUser,
} from "react-icons/fi";

import {
  EnterprisePage,
  ErrorBanner,
  LoadingState,
  MetricCard,
  PageHero,
  StatusBadge,
} from "../../../components/ui/EnterpriseUI";
import { formatDate } from "../../../components/ui/formatters";
import { getApiErrorMessage } from "../../../api/errorUtils";
import { getEmployeeById } from "../services/employeeService";

function DetailItem({ label, value }) {
  return (
    <p>
      <strong>{label}</strong>
      <span>{value || "N/A"}</span>
    </p>
  );
}

function EmployeeProfile() {
  const navigate = useNavigate();
  const [employee, setEmployee] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadProfile = async () => {
    try {
      setLoading(true);
      setError("");
      const user = JSON.parse(localStorage.getItem("user") || "null");

      if (!user?.id) {
        navigate("/login");
        return;
      }

      const response = await getEmployeeById(user.id);
      setEmployee(response.data);
    } catch (loadError) {
      console.error("Error loading profile:", loadError);
      setError(getApiErrorMessage(loadError, "Unable to load employee profile."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfile();
  }, []);

  if (loading) {
    return (
      <EnterprisePage>
        <section className="enterprise-panel">
          <LoadingState label="Loading profile..." />
        </section>
      </EnterprisePage>
    );
  }

  if (!employee) {
    return (
      <EnterprisePage>
        <ErrorBanner message={error || "Employee profile not found."} onRetry={loadProfile} />
      </EnterprisePage>
    );
  }

  const employeeName = employee.employeename || employee.employeeName || "Employee";

  return (
    <EnterprisePage>
      <PageHero
        eyebrow="Employee Profile"
        title={employeeName}
        description="Review your employee record, reporting details, contact information, and account actions."
        icon={FiUser}
        meta={
          <>
            <span>Employee #{employee.employeeid || "N/A"}</span>
            <span>{employee.department || "Department not assigned"}</span>
            <span>
              <StatusBadge status={employee.employmentStatus || "ACTIVE"} />
            </span>
          </>
        }
        actions={
          <>
            <button
              type="button"
              className="btn btn-light"
              onClick={loadProfile}
              disabled={loading}
            >
              <FiRefreshCw /> Refresh
            </button>
            <button
              type="button"
              className="btn btn-warning"
              onClick={() => navigate("/employee/change-password")}
            >
              <FiKey /> Change Password
            </button>
          </>
        }
      />

      <ErrorBanner message={error} onRetry={loadProfile} />

      <section className="enterprise-grid">
        <MetricCard
          label="Department"
          value={employee.department || "N/A"}
          helper={employee.designation || "Designation not set"}
          icon={FiBriefcase}
          tone="blue"
        />
        <MetricCard
          label="Role"
          value={employee.role || "N/A"}
          helper={employee.employmentType || "Employment type not set"}
          icon={FiUser}
          tone="green"
        />
        <MetricCard
          label="Location"
          value={employee.workLocation || "N/A"}
          helper="Current work location"
          icon={FiMapPin}
          tone="gold"
        />
        <MetricCard
          label="Email"
          value={employee.companyemail || "N/A"}
          helper={employee.phonenumber || "Phone not set"}
          icon={FiMail}
          tone="teal"
        />
      </section>

      <section className="enterprise-panel">
        <div className="enterprise-panel-header">
          <div>
            <h2>Profile Information</h2>
            <p>Core employee and assignment details from the HRMS record.</p>
          </div>
        </div>

        <div className="detail-list">
          <DetailItem label="Employee Code" value={employee.employeeCode} />
          <DetailItem label="Name" value={employeeName} />
          <DetailItem label="Department" value={employee.department} />
          <DetailItem label="Designation" value={employee.designation} />
          <DetailItem label="Role" value={employee.role} />
          <DetailItem label="Employment Type" value={employee.employmentType} />
          <DetailItem label="Employment Status" value={employee.employmentStatus} />
          <DetailItem label="Phone Number" value={employee.phonenumber} />
          <DetailItem label="Company Email" value={employee.companyemail} />
          <DetailItem label="Work Location" value={employee.workLocation} />
          <DetailItem label="Joining Date" value={formatDate(employee.joiningDate)} />
          <DetailItem
            label="Manager"
            value={employee.manager?.employeename || "Not Assigned"}
          />
          <DetailItem
            label="Shift"
            value={employee.shift?.shiftName || "Not Assigned"}
          />
          <DetailItem label="Updated At" value={formatDate(employee.updatedAt)} />
        </div>

        <div className="enterprise-actions mt-4">
          <button
            type="button"
            className="btn btn-primary"
            onClick={() => navigate("/employee/change-password")}
          >
            <FiKey /> Change Password
          </button>
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={() => navigate("/employee")}
          >
            Dashboard
          </button>
        </div>
      </section>
    </EnterprisePage>
  );
}

export default EmployeeProfile;
