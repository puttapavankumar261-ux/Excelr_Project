import { FiEdit2, FiEye, FiTrash2 } from "react-icons/fi";
import { StatusBadge } from "../../../components/ui/EnterpriseUI";

function EmployeeTable({ employees, onView, onEdit, onDelete }) {
  const getName = (employee) =>
    employee.employeename || employee.employeeName || "Unnamed Employee";

  return (
    <table className="enterprise-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Department</th>
          <th>Designation</th>
          <th>Role</th>
          <th>Status</th>
          <th>Location</th>
          <th>Actions</th>
        </tr>
      </thead>

      <tbody>
        {employees.length > 0 ? (
          employees.map((employee) => (
            <tr key={employee.employeeid}>
              <td>{employee.employeeid}</td>
              <td>
                <span className="enterprise-row-title">
                  <span className="enterprise-avatar">
                    {getName(employee).charAt(0).toUpperCase()}
                  </span>
                  <span>
                    <strong>{getName(employee)}</strong>
                    <span className="enterprise-subtext">
                      {employee.companyemail || employee.email || "No email"}
                    </span>
                  </span>
                </span>
              </td>
              <td>{employee.department || "-"}</td>
              <td>{employee.designation || "-"}</td>
              <td>{employee.role || "-"}</td>
              <td>
                <StatusBadge status={employee.employmentStatus || "ACTIVE"} />
              </td>
              <td>{employee.workLocation || "-"}</td>
              <td>
                <div className="enterprise-actions">
                  <button
                    type="button"
                    className="btn btn-outline-primary btn-sm"
                    onClick={() => onView(employee.employeeid)}
                    aria-label={`View ${getName(employee)}`}
                  >
                    <FiEye /> View
                  </button>
                  <button
                    type="button"
                    className="btn btn-outline-secondary btn-sm"
                    onClick={() => onEdit(employee.employeeid)}
                    aria-label={`Edit ${getName(employee)}`}
                  >
                    <FiEdit2 /> Edit
                  </button>
                  <button
                    type="button"
                    className="btn btn-outline-danger btn-sm"
                    onClick={() => onDelete(employee.employeeid)}
                    aria-label={`Delete ${getName(employee)}`}
                  >
                    <FiTrash2 /> Delete
                  </button>
                </div>
              </td>
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan="8">
              <div className="enterprise-state">
                <strong>No employees found</strong>
                <span>Try changing the search or filter criteria.</span>
              </div>
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
}

export default EmployeeTable;
