function EmployeeTable({ employees, onView, onEdit, onDelete }) {
  return (
    <table className="table table-hover table-bordered">
      <thead className="table-dark">
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Department</th>
          <th>Designation</th>
          <th>Role</th>
          <th>Status</th>
          <th>Location</th>
          <th width="220">Actions</th>
        </tr>
      </thead>

      <tbody>
        {employees.length > 0 ? (
          employees.map((employee) => (
            <tr key={employee.employeeid}>
              <td>{employee.employeeid}</td>
              <td>{employee.employeename}</td>
              <td>{employee.department}</td>
              <td>{employee.designation}</td>
              <td>{employee.role}</td>
              <td>{employee.employmentStatus}</td>
              <td>{employee.workLocation}</td>

              <td>
                <button
                  className="btn btn-info btn-sm me-2"
                  onClick={() => onView(employee.employeeid)}
                >
                  View
                </button>

                <button
                  className="btn btn-warning btn-sm me-2"
                  onClick={() => onEdit(employee.employeeid)}
                >
                  Edit
                </button>

                <button
                  className="btn btn-danger btn-sm"
                  onClick={() => onDelete(employee.employeeid)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan="8" className="text-center">
              No Employees Found
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
}

export default EmployeeTable;
