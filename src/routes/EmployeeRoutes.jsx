import { Routes, Route, Navigate } from "react-router-dom";

import EmployeeLayout from "../components/EmployeeComponents/EmployeeLayout";

import EmployeeDashboard from "../modules/employee/pages/EmployeeDashboard";
import EmployeeProfile from "../modules/employee/pages/EmployeeProfile";
import EmployeeAttendance from "../modules/employee/pages/EmployeeAttendance";
import EmployeeLeave from "../modules/employee/pages/EmployeeLeave";
import EmployeePayslips from "../modules/employee/pages/EmployeePayslips";
import EmployeeSettings from "../modules/employee/pages/EmployeeSettings";
import ChangePassword from "../modules/employee/pages/ChangePassword";
import EmployeePayslipDetails from "../modules/employee/pages/EmployeePayslipDetails";

function EmployeeRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="dashboard" replace />} />

      <Route
        path="dashboard"
        element={
          <EmployeeLayout>
            <EmployeeDashboard />
          </EmployeeLayout>
        }
      />

      <Route
        path="profile"
        element={
          <EmployeeLayout>
            <EmployeeProfile />
          </EmployeeLayout>
        }
      />

      <Route
        path="attendance"
        element={
          <EmployeeLayout>
            <EmployeeAttendance />
          </EmployeeLayout>
        }
      />

      <Route
        path="leave"
        element={
          <EmployeeLayout>
            <EmployeeLeave />
          </EmployeeLayout>
        }
      />

      <Route
        path="payslips"
        element={
          <EmployeeLayout>
            <EmployeePayslips />
          </EmployeeLayout>
        }
      />

      <Route
        path="settings"
        element={
          <EmployeeLayout>
            <EmployeeSettings />
          </EmployeeLayout>
        }
      />

      <Route
        path="change-password"
        element={
          <EmployeeLayout>
            <ChangePassword />
          </EmployeeLayout>
        }
      />

      <Route path="*" element={<Navigate to="dashboard" replace />} />
      <Route path="payslips/view/:id" element={<EmployeePayslipDetails />} />
    </Routes>
  );
}

export default EmployeeRoutes;
